## Fixed Function 1
public void stop() {
    if (this.runningState != STATE_RUNNING && this.runningState != STATE_SUSPENDED) {
        throw new IllegalStateException("Stopwatch is not running. ");
    }
    stopTime = System.currentTimeMillis();
    elapsedTime += stopTime - startTime;
    this.runningState = STATE_STOPPED;
} 

Explanation: In the original code, stopTime is initialized to the current time, but the elapsed time is not calculated and added to the total elapsed time. Therefore, the fix includes adding the difference between the stop and start times to the elapsed time.