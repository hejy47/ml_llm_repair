import subprocess
import logging, time

def run_cmd(cmd, cwd=None, logger=None):
    if logger == None:
        logger = logging.getLogger()
        logger.setLevel(logging.INFO)
    start_time = time.time()
    logger.info("cmd to run: {}".format(cmd))
    # # format for windows
    # cmd = cmd.replace("\\", "/")
    p = subprocess.run(cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE, cwd=cwd)
    try:
        output = p.stdout.decode("utf-8")
    except UnicodeDecodeError:
        logger.warn("cmd UnicodeDecoderError")
        output = p.stdout.decode("unicode_escape")

    return output