import os, re
import config, prompt
from utils import file_util, dataset_util, cmd_util
from dataset import Dataset

class Defects4J(Dataset.Dataset):
    def __init__(self):
        self.name = self.__class__.__name__.lower()
        super().__init__(self.name)
        self.bug_info = {
            "Chart": [i for i in range(1, 27)],
            "Closure": [i for i in range(1, 134) if i not in [63, 93]],
            "Lang": [i for i in range(1, 66) if i not in [2]],
            "Math": [i for i in range(1, 107)],
            "Mockito": [i for i in range(1, 39)],
            "Time": [i for i in range(1, 28) if i not in [21]]
        }
        self.bug_info_dir = os.path.join(config.BUG_DATA_DIR, "defects4j")
        self.get_bugs()

        self.current_bug_id = None
    
    def get_bug_info(self):
        return self.bug_info
    
    def get_bugs(self):
        for project, bug_ids in self.bug_info.items():
            for bug_id in bug_ids:
                bug_path = os.path.join(self.bug_info_dir, "{}-{}.json".format(project.lower(), bug_id))
                bug = file_util.read_json_file(bug_path)
                self.bugs["{}-{}".format(project.lower(), bug_id)] = bug
    
    def get_bug(self, project, bug_id):
        return self.bugs["{}-{}".format(project.lower(), bug_id)]

    def set_current_bug(self, project, bug_id):
        self.current_bug_id = "{}-{}".format(project.lower(), bug_id)
    
    def get_proj_dir(self):
        if self.current_bug_id == None:
            return None
        
        proj_dir = os.path.join(config.DATASET_DIR, "defects4j", self.current_bug_id)
        return proj_dir
    
    def generate_prompt(self, project, bug_id):
        bug = self.bugs["{}-{}".format(project.lower(), bug_id)]
        used_prompt = prompt.EXAMPLE_PROMPT

        used_prompt += "# There exist {} buggy function(s) in the {} project. Provide the corresponding fixes for the buggy functions.\n\n".format(len(bug), project)
        used_prompt += "# Buggy Functions\n"

        function_num = 1
        for buggy_function_name, buggy_function in bug.items():
            used_prompt += "## Buggy Function {}\n".format(function_num)
            used_prompt += buggy_function["buggy_content"]
            used_prompt += "\n\n"
            function_num += 1
        
        used_prompt += "# Fixed Functions\n"
        return used_prompt

    def validate(self, bug, fix, skip_val=True):
        if skip_val:
            return False
        else:
            all_diff_content = {}
            for buggy_function_name, buggy_function in bug.items():
                file_name = '/'.join(buggy_function_name.split('.')[:-1]) + ".java"

                buggy_function_content = buggy_function["buggy_content"]

                function_name = buggy_function_content.split('\n')[0]
                function_name = function_name.replace('(', '\(').replace(')', '\)').replace('[', '\[').replace(']', '\]').replace('{', '\{').replace('}', '\}')
                pattern = "({}.*?\n}})".format(function_name)
                result = re.search(pattern, fix, re.DOTALL)
                if result == None:
                    continue
                fixed_function_content = result[0]

                buggy_range = buggy_function["method_range"]

                if file_name not in all_diff_content:
                    all_diff_content[file_name] = [(buggy_function_content, fixed_function_content, buggy_range)]
                else:
                    all_diff_content[file_name].append((buggy_function_content, fixed_function_content, buggy_range))
            
            if all_diff_content == {}:
                return False

            for diff_file_name, diff_content in all_diff_content.items():
                project, bug_id = self.current_bug_id.split('-')
                diff_file_path = os.path.realpath(os.path.join(dataset_util.get_proj_source_dir(self.get_proj_dir(), self.name, project, bug_id), diff_file_name))
                backup_diff_file_path = os.path.join(config.TMP_DIR, diff_file_path.replace('/', '#'))
                file_util.backup_file(diff_file_path, backup_diff_file_path)

                file_lines = file_util.read_file_to_lines(diff_file_path)
                file_str = file_util.read_file_to_str(diff_file_path)
                for (buggy_function_content, fixed_function_content, buggy_range) in diff_content:
                    start_line, end_line = buggy_range.split('-')
                    start_line, end_line = int(start_line)-1, int(end_line)
                    buggy_str = ''.join(file_lines[start_line:end_line])
                    file_str = file_str.replace(buggy_str, fixed_function_content)
                file_util.write_str_to_file(file_str, diff_file_path)
            
            cmd = "defects4j test"
            result = cmd_util.run_cmd(cmd, cwd=self.get_proj_dir())

            for backup_file in os.listdir(config.TMP_DIR):
                origin_file = backup_file.replace('#', '/')
                file_util.move_file(os.path.join(config.TMP_DIR, backup_file), origin_file)
            file_util.remove_dir(config.TMP_DIR)
            
            if "Failing tests: 0" in result:
                return True
            else:
                return False
