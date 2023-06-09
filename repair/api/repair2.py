import os, time
import openai
import config
from utils import diff_util, file_util
from dataset import DatasetFactory
from api import api_request


def repair_loop(dataset, prompt, project, bug_id, bug, t_chances, stop=None, skip_val=True):
    start = time.time()
    repair_result = []
    p_diff = {}
    dataset.set_current_bug(project, bug_id)
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
            return []
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
    dataset.set_current_bug(project, bug_id)
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
            return []
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
    prompts = dataset.generate_prompts_with_single_functions(project, bug_id)
    if len(prompts) <= 1:
        return
    bug = dataset.get_bug(project, bug_id)
    for i, prompt in enumerate(prompts):
        repair_result = repair_loop_by_chat(dataset, prompt, project, bug_id, bug, chances)
        output_path = os.path.join(config.OUTPUT_DIR_MULTI_PROMPT, dataset.get_name(), "{}-{}".format(project, bug_id), "{}-{}_{}.json".format(project, bug_id, i))
        file_util.write_json_file(repair_result, output_path)

def repair_all(dataset, chances):
    bug_info = dataset.get_bug_info()
    for project, bug_ids in bug_info.items():
        for bug_id in bug_ids:
            repair_single(dataset, project, bug_id, chances)

def combine_repairs(repairs):
    if len(repairs) == 1:
        return [{"output":x["output"]} for x in repairs[0]]
    else:
        result = []
        for x in repairs[0]:
            for y in combine_repairs(repairs[1:]):
                tmp_result = x["output"] + "\n\n" + y["output"]
                result.append({"output": tmp_result})
        return result

def apply_patch_and_validate(dataset):
    if dataset.get_name().lower() in ["defects4j"]:
        suffix_name = ".java"
    bug_info = dataset.get_bug_info()
    for project, bug_ids in bug_info.items():
        for bug_id in bug_ids:
            if project.lower() == "chart" and bug_id == 2:
                continue
            repair_dir = os.path.join(config.OUTPUT_DIR_MULTI_PROMPT, dataset.get_name(), "{}-{}".format(project, bug_id))
            if not os.path.exists(repair_dir):
                continue
            print("validating {} {}".format(project, bug_id))
            bug = dataset.get_bug(project, bug_id)
            if bug == {}:
                continue
            dataset.set_current_bug(project, bug_id)

            all_repairs = []
            for repair_file in os.listdir(repair_dir):
                tmp_repairs = file_util.read_json_file(os.path.join(repair_dir, repair_file))
                all_repairs.append(tmp_repairs)
            all_repairs = combine_repairs(all_repairs)

            output_path = os.path.join(config.OUTPUT2_DIR_MULTI_PROMPT, dataset.get_name(), "{}-{}".format(project, bug_id))
            repair_result = []
            valid_result = ""
            for i, repair in enumerate(all_repairs[:10000]):
                output = repair["output"]
                valid = dataset.validate(bug, output, skip_val=False)
                valid_result += "{}-{}_{}:{}\n".format(project, bug_id, i+1, str(valid))
                diff = diff_util.get_unified_diff(bug, output, suffix_name)
                file_util.write_str_to_file(output, os.path.join(output_path, "{}-{}_{}".format(project, bug_id, i+1)+suffix_name))
                file_util.write_str_to_file(diff, os.path.join(output_path, "{}-{}_{}.diff".format(project, bug_id, i+1)))

                repair_result.append({'output': output,
                                    'diff': diff,
                                    'valid': valid})
            file_util.write_str_to_file(valid_result, os.path.join(output_path, "{}-{}_validation.txt".format(project, bug_id)))
            file_util.write_json_file(repair_result, os.path.join(output_path, "{}-{}.json".format(project, bug_id)))