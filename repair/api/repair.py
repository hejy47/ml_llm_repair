import os, json, time, argparse
import openai
import config
from utils import diff_util, file_util
from dataset import DatasetFactory
from api_request import create_openai_config, request_engine

openai.api_key = open(config.API_KEY_FILE, "r").read().strip()

def repair_loop(dataset, prompt, project, bug_id, bug, t_chances, stop=None, skip_val=True):
    start = time.time()
    repair_result = []
    p_diff = {}
    print("Repairing bug {} {} ... ".format(project, bug_id))
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
        diff = diff_util.get_unified_diff(bug, output)
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
                              'valid': dataset.validate(bug, output, skip_val=skip_val),
                              'num': 1})

    end = time.time()
    print("{} Unique Patches Generated in {}s".format(len(repair_result), end - start))

    return repair_result

def repair_single(dataset, project, bug_id):
    prompt = dataset.generate_prompt(project, bug_id)
    bug = dataset.get_bug(project, bug_id)
    repair_result = repair_loop(dataset, prompt, project, bug_id, bug)
    output_path = os.path.join(config.OUTPUT_DIR, dataset.get_name(), "{}-{}.json".format(project, bug_id))
    file_util.write_str_to_file(json.dumps(repair_result, output_path))

def repair_all(dataset):
    bug_info = dataset.get_bug_info()
    for project, bug_ids in bug_info.items():
        for bug_id in bug_ids:
            repair_single(dataset, project, bug_id)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--dataset", type=str, default="defects4j",
                        help="Dataset to use, current support: defects4j, quixbug-python, manybugs")
    parser.add_argument("--chances", type=int, default=1)
    parser.add_argument("--skip_val", action="store_true", default=False)
    parser.add_argument("--folder", type=str, default="Results/test")
    args = parser.parse_args()
    dataset = DatasetFactory.create_dataset(args.dataset)