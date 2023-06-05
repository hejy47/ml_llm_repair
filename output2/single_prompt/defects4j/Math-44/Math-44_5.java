protected double acceptStep(final AbstractStepInterpolator interpolator, final double[] y, final double[] yDot, final double tEnd) throws MathIllegalStateException {
    double previousT = interpolator.getGlobalPreviousTime();
    final double currentT = interpolator.getGlobalCurrentTime();
    resetOccurred = false;
    // initialize the events states if needed
    if (!statesInitialized) {
        for (EventState state : eventsStates) {
            state.reinitializeBegin(interpolator);
        }
        statesInitialized = true;
    }
    // search for next events that may occur during the step
    final int orderingSign = interpolator.isForward() ? +1 : -1;
    SortedSet<EventState> occurringEvents = new TreeSet<EventState>(new Comparator<EventState>() {
                public int compare(EventState es0, EventState es1) {
            return orderingSign * Double.compare(es0.getEventTime(), es1.getEventTime());
        }
    });
    for (final EventState state : eventsStates) {
        if (state.evaluateStep(interpolator)) {
            // the event occurs during the current step
            occurringEvents.add(state);
        }
    }
    while (!occurringEvents.isEmpty()) {
        // handle the chronologically first event
        final Iterator<EventState> iterator = occurringEvents.iterator();
        final EventState currentEvent = iterator.next();
        iterator.remove();
        // restrict the interpolator to the first part of the step, up to the event
        final double eventT = currentEvent.getEventTime();
        interpolator.setInterpolatedTime(eventT);
        interpolator.setInterpolatedDerivatives(currentEvent.getEventStateDerivatives());
        // trigger the event
        final double[] eventY = currentEvent.eventOccurred(interpolator, isLastStep);
        if (isLastStep) {
            // the event asked to stop integration
            System.arraycopy(eventY, 0, y, 0, y.length);
            return eventT;
        }
        // handle the first part of the step, up to the event
        for (final StepHandler handler : stepHandlers) {
            handler.handleStep(interpolator, isLastStep);
        }
        if (currentEvent.reset(eventT, eventY)) {
            // some event handler has triggered changes that
            // invalidate the derivatives, we need to recompute them
            System.arraycopy(eventY, 0, y, 0, y.length);
            computeDerivatives(eventT, y, yDot);
            resetOccurred = true;
            return eventT;
        }
        // prepare handling of the remaining part of the step
        previousT = eventT;
        interpolator.reinitialize(eventT, stepSize, y, yDot);
        interpolator.storeTime(eventT);
        // search for next events that may occur during the step
        occurringEvents.clear();
        for (final EventState state : eventsStates) {
            if (state.evaluateStep(interpolator)) {
                // the event occurs during the current step
                occurringEvents.add(state);
            }
        }
    }
    interpolator.setInterpolatedTime(currentT);
    final double[] currentY = interpolator.getInterpolatedState();
    for (final EventState state : eventsStates) {
        state.stepAccepted(currentT, currentY);
        isLastStep = isLastStep || state.stop();
    }
    isLastStep = isLastStep || Precision.equals(currentT, tEnd, 1);
    // handle the remaining part of the step, after all events if any
    for (StepHandler handler : stepHandlers) {
        handler.handleStep(interpolator, isLastStep);
    }
    return currentT;
}