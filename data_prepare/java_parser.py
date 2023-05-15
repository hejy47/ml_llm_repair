import jpype
import os, re
import json

if __name__ == "__main__":
    jarpath = ""

def parse_java_file(java_file_path):
    StaticJavaParser = jpype.JClass("com.github.javaparser.StaticJavaParser")
    with open(java_file_path, "r", errors="ignore") as file:
        content = file.read()
        cu = StaticJavaParser.parse(content)
        return cu

def get_method_by_name(java_tree, method_name, fl_lines):
    ConstructorDeclaration = jpype.JClass("com.github.javaparser.ast.body.ConstructorDeclaration")
    MethodDeclaration = jpype.JClass("com.github.javaparser.ast.body.MethodDeclaration")
    constructors = java_tree.getChildNodesByType(ConstructorDeclaration)
    methods = java_tree.getChildNodesByType(MethodDeclaration)

    all_fl_lines = []
    suspicious_methods = []
    for method in constructors + methods:
        if method.getName().toString() == method_name:
            start_line = method.getRange().get().begin.line
            end_line = method.getRange().get().end.line
            map_fl_lines = []
            for fl_line in fl_lines:
                if int(fl_line) >= start_line and int(fl_line) <= end_line:
                    map_fl_lines.append(fl_line)
                    all_fl_lines.append(fl_line)
            if map_fl_lines != []:
                method_range = (start_line, end_line)
                method_content = str(method.removeComment().toString())
                suspicious_methods.append((method_range, method_content, map_fl_lines))
            if all_fl_lines == fl_lines:
                return suspicious_methods
    return suspicious_methods

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
        if '$' in method_lists[-2]:
            index = method_lists[-2].index('$')
            method_lists[-2] = method_lists[-2][:index]
        method_path, method_name = '/'.join(method_lists[:-1]) + ".java", method_lists[-1]
        if method_path not in suspicious_files_methods:
            suspicious_files_methods[method_path] = [(method_name, fl_lines)]
        else:
            suspicious_files_methods[method_path].append((method_name, fl_lines))
    
    suspicious_methods_content = {}
    for suspicious_file, suspicious_methods in suspicious_files_methods.items():
        tree = parse_java_file(os.path.join(project_dir, suspicious_file))

        for (method_name, fl_lines) in suspicious_methods:
            suspicious_method_contents = get_method_by_name(tree, method_name, fl_lines)
            for i, (method_position, method_content, map_fl_lines) in enumerate(suspicious_method_contents):
                key = "{}.{}".format(suspicious_file[:-5].replace("/", "."), method_name)
                if key in suspicious_methods_content: key = key + "{}".format(i)
                suspicious_methods_content[key] = {
                    "buggy_content": method_content,
                    "method_range": "{}-{}".format(method_position[0], method_position[1]),
                    "fault_locations": ",".join(map_fl_lines)
                }
    return suspicious_methods_content
    
if __name__ == "__main__":
    jar_path = "/mnt/Code/NPR/mfl_llm_apr/data_prepare/JavaParser/javaparser-core-3.25.4-SNAPSHOT.jar"
    jpype.startJVM(classpath=jar_path)

    d4j_v1_info = {
        "Chart": 26,
        "Closure": 133,
        "Lang": 65,
        "Math": 106,
        "Time": 27
    }
    for project, sum in d4j_v1_info.items():
        for i in range(1, sum+1):
            if project == "Closure" and i in [63, 93]: continue
            if project == "Lang" and i == 2: continue
            if project == "Time" and i == 21: continue
            project_dir0 = "/mnt/Code/NPR/Dataset/d4j-v1.4/{}/buggy_version/{}_{}/".format(project, project.lower(), i)
            if project == "Chart":
                project_dir = os.path.join(project_dir0, "source")
            elif project == "Closure" or project == "Mockito":
                project_dir = os.path.join(project_dir0, "src")
            elif project == "Lang" or project == "Math" or project == "Time":
                project_dir = os.path.join(project_dir0, "src/main/java")
                if project == "Lang" and i >= 36 or project == "Math" and i >= 85:
                    project_dir = os.path.join(project_dir0, "src/java")

            fl_result = "/mnt/Code/NPR/mfl_llm_apr/location/defects4j/groundtruth/{}/{}".format(project.lower(), i)
            output_path = "/mnt/Code/NPR/mfl_llm_apr/data/defects4j/{}-{}.json".format(project.lower(), i)
            suspicious_methods_content = get_suspicious_methods(project_dir, fl_result)
            with open(output_path, "w") as f:
                json.dump(suspicious_methods_content, f)