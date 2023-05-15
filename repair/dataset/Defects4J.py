import os
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
    
    def get_bugs(self):
        pass