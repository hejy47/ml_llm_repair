
from dataset import Defects4J

def create_dataset(dataset_name):
    dataset = None
    if dataset_name.lower() == "defects4j":
        dataset = Defects4J.Defects4J()
    return dataset