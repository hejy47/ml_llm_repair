import os

def get_proj_source_dir(project_dir, dataset, project, bug_id):
    proj_source_dir = None
    if dataset.lower() == "defects4j":
        if project == "Chart":
            proj_source_dir = os.path.join(project_dir, "source")
        elif project == "Closure" or project == "Mockito":
            proj_source_dir = os.path.join(project_dir, "src")
        elif project == "Lang" or project == "Math" or project == "Time":
            proj_source_dir = os.path.join(project_dir, "src/main/java")
            if project == "Lang" and bug_id >= 36 or project == "Math" and bug_id >= 85:
                proj_source_dir = os.path.join(project_dir, "src/java")
    return proj_source_dir