import os, json
import config
from dataset import Dataset

class Defects4J(Dataset.Dataset):
    def __init__(self):
        self.name = self.__class__.__name__.lower()
        super().__init__(self.name)
        self.bug_info = {
            "Chart": [i for i in range(1, 27)],
            "Closure": [i for i in range(1, 134)],
            "Lang": [i for i in range(1, 66)],
            "Math": [i for i in range(1, 106)],
            "Time": [i for i in range(1, 27)]
        }
        self.bug_info_dir = os.path.join(config.BUG_DATA_DIR, "defects4j")
        self.get_bugs()
    
    def get_bug_info(self):
        return self.bug_info
    
    def get_bugs(self):
        for project, bug_ids in self.bug_info.items():
            for bug_id in bug_ids:
                bug_path = os.path.join(self.bug_info_dir, "{}-{}.json".format(project.lower(), bug_id))
                bug = json.loads(bug_path)
                self.bugs["{}-{}".format(project.lower(), bug_id)] = bug
    
    def get_bug(self, project, bug_id):
        return self.bugs["{}-{}".format(project.lower(), bug_id)]
    
    def generate_prompt(self, project, bug_id):
        bug = self.bugs["{}-{}".format(project.lower(), bug_id)]
        prompt = "# There exist {} buggy functions in the {} project. Provide the corresponding fixes for the buggy functions.\n\n".format(len(bug), project)
        prompt += "# Buggy Functions\n"

        function_num = 1
        for buggy_function_name, buggy_function in bug.items():
            prompt += "## Buggy Function {}\n".format(function_num)
            prompt += buggy_function["buggy_content"]
            prompt += "\n"
        
        prompt += "# Fixed Functions\n"
        return prompt

    def validate(self, bug, fix, skip_val=True):
        if skip_val:
            return False
        pass
