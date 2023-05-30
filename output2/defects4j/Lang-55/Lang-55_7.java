## Fixed Function 1
public void stop() {
    if (this.runningState == STATE_NOT_STARTED) {
        throw new IllegalStateException("Stopwatch has not been started. ");
    } else if (this.runningState == STATE_STOPPED) {
        throw new IllegalStateException("Stopwatch has already been stopped. ");
    }
    stopTime = System.currentTimeMillis();
    this.runningState = STATE_STOPPED;
} 

The fix corrects the check if the stopwatch has been started or not. It also adds a check to ensure that the stopwatch has not already been stopped.