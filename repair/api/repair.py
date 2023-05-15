import openai
import json
from api_request import create_openai_config

API_KEY_FILE = "api_key.txt"
openai.api_key = open(API_KEY_FILE, "r").read().strip()

def repair_loop(prompt, file_name, folder, bug, t_chances, stop="# Provide a fix for the buggy function",
                skip_val=True) -> (bool, bool, list):
    start = time.time()
    repair_result = []
    p_diff = {}
    print("Repairing bug {} ... ".format(file_name.split(".")[0]))
    print(prompt)
    temperature = 0.8
    top_p = 0.95
    config = create_openai_config(prompt=prompt, stop=stop, temperature=temperature, top_p=top_p)
    total_times = 0
    while t_chances > 0:
        total_times += 1
        t_chances -= 1
        print("Try: {}".format(total_times))
        ret = request_engine(config)
        if ret is None:
            return False, False, []
        output = ret["choices"][0]['text'].strip()
        diff = get_unified_diff(bug['buggy'], output)
        finish_reason = ret["choices"][0]['finish_reason']
        if finish_reason != "stop":
            continue
        if diff in p_diff:
            repair_result[p_diff[diff]]['num'] += 1
            continue
        p_diff[diff] = len(repair_result)
        print(diff)
        repair_result.append({'output': output,
                              'diff': diff,
                              'finish_reason': finish_reason,
                              'entropy': (-sum(ret["choices"][0]['logprobs']["token_logprobs"]) / len(ret["choices"][0]['logprobs']["token_logprobs"]),
                                          -sum(ret["choices"][0]['logprobs']["token_logprobs"])),
                              'valid': _run_validation(file_name.split(".")[0],
                                                       file_name.split(".")[0] + "_" +
                                                       str(len(repair_result)) + "." + file_name.split(".")[1],
                                                       folder, output, skip_val=skip_val),
                              'num': 1})

    end = time.time()
    print("{} Unique Patches Generated in {}s".format(len(repair_result), end - start))

    return False, False, repair_result

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--dataset", type=str, default="defects4j",
                        help="Dataset to use, current support: defects4j, quixbug-python, manybugs")
    parser.add_argument("--chances", type=int, default=1)
    parser.add_argument("--skip_val", action="store_true", default=False)
    parser.add_argument("--folder", type=str, default="Results/test")
    args = parser.parse_args()
    dataset = 