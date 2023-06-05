import os

PROJECT_PATH = os.path.realpath(os.path.join(os.path.abspath(os.path.dirname(__file__)), ".."))

DATASET_DIR = os.path.join(PROJECT_PATH, "/mnt/Dataset")

BUG_DATA_DIR = os.path.join(PROJECT_PATH, "data")
OUTPUT_DIR_SINGLE_PROMPT = os.path.join(PROJECT_PATH, "output/single_prompt") # output dir of single prompt with multi functions
OUTPUT2_DIR_SINGLE_PROMPT = os.path.join(PROJECT_PATH, "output2/single_prompt")

OUTPUT_DIR_MULTI_PROMPT = os.path.join(PROJECT_PATH, "output/multi_prompt") # output dir of multi prompts with single function
OUTPUT2_DIR_MULTI_PROMPT = os.path.join(PROJECT_PATH, "output2/multi_prompt")

TMP_DIR = os.path.join(PROJECT_PATH, "tmp")
API_KEY_FILE = os.path.join(PROJECT_PATH, "repair/api/api_key.txt")

MAX_TEST_TIME = 300