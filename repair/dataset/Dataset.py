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
    def generate_prompt(self):
        pass

    @abc.abstractclassmethod
    def validate(self):
        pass
