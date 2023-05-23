import os, time
import openai
import config
from utils import diff_util, file_util
from dataset import DatasetFactory
from api import api_request

openai.api_key = open(config.API_KEY_FILE, "r").read().strip()

def repair_loop(dataset, prompt, project, bug_id, bug, t_chances, stop=None, skip_val=True):
    start = time.time()
    repair_result = []
    p_diff = {}
    print("Repairing bug {} {} ... ".format(project, bug_id))
    print(prompt)
    temperature = 0.8
    top_p = 0.95
    config = api_request.create_openai_config(prompt=prompt, stop=stop, temperature=temperature, top_p=top_p)
    total_times = 0
    while t_chances > 0:
        total_times += 1
        t_chances -= 1
        print("Try: {}".format(total_times))
        ret = api_request.request_engine(config)
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

def repair_loop_by_chat(dataset, prompt, project, bug_id, bug, t_chances, stop=None, skip_val=True):
    start = time.time()
    repair_result = []
    p_diff = {}
    print("Repairing bug {} {} ... ".format(project, bug_id))
    print(prompt)
    config = api_request.create_openai_chat_config(prompt)
    total_times = 0
    while t_chances > 0:
        total_times += 1
        t_chances -= 1
        print("Try: {}".format(total_times))
        ret = api_request.request_chat_engine(config)
        if ret is None:
            return False, False, []
        output = ret["choices"][0]["message"]["content"]
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
                              'valid': dataset.validate(bug, output, skip_val=skip_val),
                              'num': 1})

    end = time.time()
    print("{} Unique Patches Generated in {}s".format(len(repair_result), end - start))

    return repair_result

def repair_single(dataset, project, bug_id, chances):
    prompt = dataset.generate_prompt(project, bug_id)
    bug = dataset.get_bug(project, bug_id)
    repair_result = repair_loop_by_chat(dataset, prompt, project, bug_id, bug, chances)
    output_path = os.path.join(config.OUTPUT_DIR, dataset.get_name(), "{}-{}.json".format(project, bug_id))
    file_util.write_json_file(repair_result, output_path)

def repair_all(dataset, chances):
    bug_info = dataset.get_bug_info()
    for project, bug_ids in bug_info.items():
        if project.lower() != "mockito": continue
        for bug_id in bug_ids:
            repair_single(dataset, project, bug_id, chances)

