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

def get_method_by_name(java_tree, fl_lines):
    ConstructorDeclaration = jpype.JClass("com.github.javaparser.ast.body.ConstructorDeclaration")
    MethodDeclaration = jpype.JClass("com.github.javaparser.ast.body.MethodDeclaration")
    constructors = java_tree.getChildNodesByType(ConstructorDeclaration)
    methods = java_tree.getChildNodesByType(MethodDeclaration)

    all_fl_lines = []
    suspicious_methods = []
    for method in constructors + methods:
        method_name = method.getName().toString()
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
            method_comments = re.findall("/\*.*?\*/\n", method_content, re.DOTALL)
            if method_comments:
                for method_comment in method_comments:
                    method_content = method_content.replace(method_comment, "")
            suspicious_methods.append((method_name, method_range, method_content, map_fl_lines))
        if all_fl_lines == fl_lines:
            return suspicious_methods
    return suspicious_methods

def get_suspicious_methods(project_dir, fl_result):
    suspicious_files = {}
    with open(fl_result, "r") as file:
        for l in file:
            pattern = "(.*?)#(\d+)"
            ret = re.search(pattern, l)
            fl_file, fl_line = ret[1], ret[2]
            fl_file = fl_file.replace('.', '/') + ".java"
            if fl_file not in suspicious_files:
                suspicious_files[fl_file] = [fl_line]
            else:
                suspicious_files[fl_file].append(fl_line)

    suspicious_methods = {}
    for suspicious_file, fl_lines in suspicious_files.items():
        if not os.path.exists(os.path.join(project_dir, suspicious_file)):
            continue
        try:
            tree = parse_java_file(os.path.join(project_dir, suspicious_file))

            suspicious_method_contents = get_method_by_name(tree, fl_lines)
            for i, (method_name, method_position, method_content, map_fl_lines) in enumerate(suspicious_method_contents):
                key = "{}.{}".format(suspicious_file[:-5].replace("/", "."), method_name)
                if key in suspicious_methods: key = key + "{}".format(i)
                suspicious_methods[key] = {
                    "buggy_content": method_content,
                    "method_range": "{}-{}".format(method_position[0], method_position[1]),
                    "fault_locations": ",".join(map_fl_lines)
                }
        except:
            continue
    return suspicious_methods
    
if __name__ == "__main__":
    jar_path = "/mnt/Code/NPR/mfl_llm_apr/data_prepare/JavaParser/javaparser-core-3.25.4-SNAPSHOT.jar"
    jpype.startJVM(classpath=jar_path)

    d4jv1_info = {
        "Chart": [i for i in range(1, 27)],
        "Closure": [i for i in range(1, 134) if i not in [63, 93]],
        "Lang": [i for i in range(1, 66) if i not in [2]],
        "Math": [i for i in range(1, 107)],
        "Mockito": [i for i in range(1, 39)],
        "Time": [i for i in range(1, 28) if i not in [21]]
    }
    
    d4j_info = {
        "Chart": [i for i in range(1, 27)],
        "Cli": [i for i in range(1, 41) if i not in [6]],
        "Closure": [i for i in range(1, 177) if i not in [63, 93]],
        "Codec": [i for i in range(1, 19)],
        "Collections": [i for i in range(25, 29)],
        "Compress": [i for i in range(1, 48)],
        "Csv": [i for i in range(1, 17)],
        "Gson": [i for i in range(1, 19)],
        "JacksonCore": [i for i in range(1, 27)],
        "JacksonDatabind": [i for i in range(1, 113)],
        "JacksonXml": [i for i in range(1, 7)],
        "Jsoup": [i for i in range(1, 94)],
        "JxPath": [i for i in range(1, 23)],
        "Lang": [i for i in range(1, 66) if i not in [2]],
        "Math": [i for i in range(1, 107)],
        "Mockito": [i for i in range(1, 39)],
        "Time": [i for i in range(1, 28) if i not in [21]]
    }
    for project, bug_ids in d4j_info.items():
        for bug_id in bug_ids:
            if project in d4jv1_info and bug_id in d4jv1_info[project]:
                continue
            project_dir0 = "/mnt/Dataset/defects4j/{}-{}/".format(project.lower(), bug_id)
            source_dir = "source"
            if project == "Chart":
                source_dir = "source"
            elif project in ["Closure", "Mockito"]:
                source_dir = "src"
            elif project in ["Lang", "Math", "Time", "Collections", "Compress", "Csv", "JacksonCore", "JacksonDatabind", "JacksonXml", "Jsoup"]:
                source_dir = "src/main/java"
                if project == "Lang" and bug_id >= 36 or project == "Math" and bug_id >= 85:
                    source_dir = "src/java"
            elif project in ["Cli", "Codec", "JxPath"]:
                source_dir = "src/java"
                if project == "Cli" and bug_id >= 30 or project == "Codec" and bug_id >= 11:
                    source_dir = "src/main/java"
            elif project == "Gson":
                source_dir = "gson/src/main/java"
            project_dir = os.path.join(project_dir0, source_dir)

            fl_result = "/mnt/Code/mfl_llm_apr/location/defects4j/manual/{}/{}.txt".format(project.lower(), bug_id)
            output_path = "/mnt/Code/mfl_llm_apr/data/defects4j/{}-{}.json".format(project.lower(), bug_id)
            suspicious_methods_content = get_suspicious_methods(project_dir, fl_result)
            with open(output_path, "w") as f:
                json.dump(suspicious_methods_content, f)