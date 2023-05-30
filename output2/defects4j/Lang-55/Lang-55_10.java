## Fixed Function 1
public void stop() {
    if (this.runningState == STATE_STOPPED) {
        throw new IllegalStateException("Stopwatch is already stopped.");
    }
    if (this.runningState != STATE_RUNNING && this.runningState != STATE_SUSPENDED) {
        throw new IllegalStateException("Stopwatch is not running or suspended.");
    }
    stopTime = System.currentTimeMillis();
    this.runningState = STATE_STOPPED;
}