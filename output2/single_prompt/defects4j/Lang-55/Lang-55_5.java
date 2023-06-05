## Fixed Function 1
public void stop() {
    if (this.runningState != STATE_RUNNING && this.runningState != STATE_SUSPENDED) {
        throw new IllegalStateException("Stopwatch is not running. ");
    }
    stopTime = System.currentTimeMillis();
    this.elapsedTime = stopTime - startTime; // added line
    this.runningState = STATE_STOPPED;
}