import javalang
import os, re, json

def parse_java_file(java_file_path):
    with open(java_file_path, "r") as file:
        content = file.read()
        java_tree = javalang.parse.parse(content)
        return java_tree

def get_method_by_name(java_tree, method_name):
    package_declarations = java_tree.children[0]
    imports = java_tree.children[1]
    class_declarations = java_tree.children[2]
    for class_declaration in class_declarations:
        for method_declaration in class_declaration.methods:
            if method_declaration.name == method_name:
                return method_declaration
    return None

def get_suspicious_methods(project_dir, fl_result):
    suspicious_methods = {}
    with open(fl_result, "r") as file:
        for l in file:
            pattern = "(.*?):(\d+)"
            ret = re.search(pattern, l)
            method, fl_line = ret[1], ret[2]
            if method not in suspicious_methods:
                suspicious_methods[method] = [fl_line]
            else:
                suspicious_methods[method].append(fl_line)
    
    suspicious_files_methods = {}
    for method, fl_lines in suspicious_methods.items():
        method_lists = method.split('.')
        method_path, method_name = '/'.join(method_lists[:-1]) + ".java", method_lists[-1]
        if method_path not in suspicious_files_methods:
            suspicious_files_methods[method_path] = [(method_name, fl_lines)]
        else:
            suspicious_files_methods[method_path].append((method_name, fl_lines))
    
    suspicious_methods_content = {}
    for suspicious_file, suspicious_methods in suspicious_files_methods.items():
        tree = parse_java_file(os.path.join(project_dir, suspicious_file))

        for (method_name, fl_lines) in suspicious_methods:
            method_tree = get_method_by_name(tree, method_name)
            method_position = (method_tree.position.line, method_tree.position.line + method_tree.position.column)
            method_content = ""
            with open(os.path.join(project_dir, suspicious_file), "r") as f:
                content_list = f.readlines()
                method_content = "\n".join(content_list[method_position[0]-1:method_position[1]-1])
            suspicious_file[:-5].replace("/", ".")
            suspicious_methods_content["{}.{}".format(suspicious_file[:-5].replace("/", "."), method_name)] = {
                "buggy_content": method_content,
                "method_range": "{}-{}".format(method_position[0], method_position[1]),
                "fault_locations": ",".join(fl_lines)
            }
    return suspicious_methods_content
    
if __name__ == "__main__":
    project_dir = "/mnt/Code/NPR/Dataset/d4j-v1.4/Chart/buggy_version/chart_2/source"
    fl_result = "/mnt/Code/NPR/mfl_llm_apr/location/defects4j/groundtruth/chart/2"
    output_path = "/mnt/Code/NPR/mfl_llm_apr/data/defects4j/chart-2.json"
    suspicious_methods_content = get_suspicious_methods(project_dir, fl_result)
    with open(output_path, "w") as f:
        json.dump(suspicious_methods_content, f)