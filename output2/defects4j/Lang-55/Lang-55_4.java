## Fixed Function 1
public void stop() {
    if (this.runningState != STATE_RUNNING && this.runningState != STATE_SUSPENDED) {
        throw new IllegalStateException("Stopwatch is not running.");
    } else {
        stopTime = System.currentTimeMillis();
        this.runningState = STATE_STOPPED;
    }
}