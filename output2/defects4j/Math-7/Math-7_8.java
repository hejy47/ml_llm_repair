protected double acceptStep(final AbstractStepInterpolator interpolator, final double[] y, final double[] yDot, final double tEnd) throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
    double previousT = interpolator.getGlobalPreviousTime();
    final double currentT = interpolator.getGlobalCurrentTime();
    // initialize the events states if needed
    if (!statesInitialized) {
        for (EventState state : eventsStates) {
            state.reinitializeBegin(interpolator);
        }
        statesInitialized = true;
    }
    // search for next events that may occur during the step
    final int orderingSign = interpolator.isForward() ? +1 : -1;
    SortedSet<EventState> occuringEvents = new TreeSet<EventState>(new Comparator<EventState>() {

        public int compare(EventState es0, EventState es1) {
            return orderingSign * Double.compare(es0.getEventTime(), es1.getEventTime());
        }
    });

    for (final EventState state : eventsStates) {
        if (state.evaluateStep(interpolator)) {
            // the event occurs during the current step
            occuringEvents.add(state);
        }
    }

    while (!occuringEvents.isEmpty()) {
        // handle the chronologically first event
        final Iterator<EventState> iterator = occuringEvents.iterator();
        final EventState currentEvent = iterator.next();
        iterator.remove();
        // restrict the interpolator to the first part of the step, up to the event
        final double eventT = currentEvent.getEventTime();
        interpolator.setSoftPreviousTime(previousT);
        interpolator.setSoftCurrentTime(eventT);
        // get state at event time
        interpolator.setInterpolatedTime(eventT);
        final double[] eventY = interpolator.getInterpolatedState().clone();
        // advance all event states to current time
        currentEvent.stepAccepted(eventT, eventY);
        isLastStep = currentEvent.stop();
        // handle the first part of the step, up to the event
        for (final StepHandler handler : stepHandlers) {
            handler.handleStep(interpolator, isLastStep);
        }
        if (isLastStep) {
            // the event asked to stop integration
            System.arraycopy(eventY, 0, y, 0, y.length);
            for (final EventState remaining : occuringEvents) {
                remaining.stepAccepted(eventT, eventY);
            }
            return eventT;
        }
        boolean needReset = currentEvent.reset(eventT, eventY, interpolator.isForward());
        if (needReset) {
            // some event handler has triggered changes that
            // invalidate the derivatives, we need to recompute them
            interpolator.reinitializeBegin(eventT, eventY, yDot);
            System.arraycopy(eventY, 0, y, 0, y.length);
            computeDerivatives(eventT, y, yDot);
            resetOccurred = true;
            for (final EventState remaining : occuringEvents) {
                remaining.stepAccepted(eventT, eventY);
            }
            return eventT;
        }
        // prepare handling of the remaining part of the step
        previousT = eventT;
        interpolator.setSoftPreviousTime(eventT);
        interpolator.setSoftCurrentTime(currentT);
        // check if the same event occurs again in the remaining part of the step
        if (currentEvent.evaluateStep(interpolator)) {
            // the event occurs during the current step
            occuringEvents.add(currentEvent);
        }
    }
    // last part of the step, after the last event
    interpolator.setSoftPreviousTime(previousT);
    interpolator.setSoftCurrentTime(currentT);
    interpolator.setInterpolatedTime(currentT);
    System.arraycopy(interpolator.getInterpolatedState(), 0, y, 0, y.length);
    for (final EventState state : eventsStates) {
        state.stepAccepted(currentT, interpolator.getInterpolatedState());
        isLastStep = isLastStep || state.stop();
    }
    isLastStep = isLastStep || Precision.equals(currentT, tEnd, 1);
    // handle the remaining part of the step, after all events if any
    for (StepHandler handler : stepHandlers) {
        handler.handleStep(interpolator, isLastStep);
    }
    return currentT;
}