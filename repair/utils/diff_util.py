import re, os
from difflib import unified_diff

def get_unified_diff(buggy_content, fixed_content, suffix_name=".java"):
    pattern = re.compile("## Fixed Function \d+\n(.*?)\n\n", re.DOTALL)
    result = pattern.findall(fixed_content+"\n\n")

    all_diff_content = ""
    function_num = 0
    for buggy_function_name, buggy_function in buggy_content.items():
        file_name = '/'.join(buggy_function_name.split('.')[:-1]) + suffix_name + buggy_function_name.split('.')[-1]

        buggy_function_content = buggy_function["buggy_content"]
        fixed_function_content = result[function_num]

        diff_content = ""
        for line in unified_diff(buggy_function_content.split('\n'), fixed_function_content.split('\n'), lineterm='', fromfile=file_name, tofile=file_name):
            diff_content += line + '\n'
        
        all_diff_content += diff_content
        function_num += 1
    
    return all_diff_content