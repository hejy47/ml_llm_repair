import argparse
from dataset import DatasetFactory
from api import repair

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--dataset", type=str, default="defects4j",
                        help="Dataset to use, current support: defects4j, quixbug-python, manybugs")
    parser.add_argument("--chances", type=int, default=1)
    parser.add_argument("--skip_val", action="store_true", default=False)
    parser.add_argument("--folder", type=str, default="Results/test")
    args = parser.parse_args()
    dataset = DatasetFactory.create_dataset(args.dataset)
    # repair.repair_single(dataset, "Chart", 7, args.chances)
    repair.repair_all(dataset, args.chances)
    # repair.apply_patch_and_validate(dataset)

if __name__ == "__main__":
    main()