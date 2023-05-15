import os

PROJECT_PATH = os.path.realpath(os.path.join(os.path.abspath(os.path.dirname(__file__)), ".."))

BUG_DATA_DIR = os.path.join(PROJECT_PATH, "data")
OUTPUT_DIR = os.path.join(PROJECT_PATH, "output")
API_KEY_FILE = os.path.join(PROJECT_PATH, "repair/api/api_key.txt")