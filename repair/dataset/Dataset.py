import abc

class Dataset:
    def __init__(self, name):
        self.name = name
        self.bugs = {}
    
    def get_name(self):
        return self.name

    @abc.abstractclassmethod
    def get_bugs(self):
        pass

    @abc.abstractclassmethod
    def generate_prompt_with_multi_functions(self, project, bug_id):
        pass

    @abc.abstractclassmethod
    def generate_prompts_with_single_function(self, project, bug_id):
        pass

    @abc.abstractclassmethod
    def validate(self, bug, fix, skip_val=True):
        pass
