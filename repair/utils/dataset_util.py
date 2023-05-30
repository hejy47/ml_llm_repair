import os

def get_proj_source_dir(project_dir, dataset, project, bug_id):
    proj_source_dir = None
    if dataset.lower() == "defects4j":
        if project.lower() == "chart":
            proj_source_dir = os.path.join(project_dir, "source")
        elif project.lower() == "closure" or project.lower() == "mockito":
            proj_source_dir = os.path.join(project_dir, "src")
        elif project.lower() == "lang" or project.lower() == "math" or project.lower() == "time":
            proj_source_dir = os.path.join(project_dir, "src/main/java")
            if project.lower() == "lang" and int(bug_id) >= 36 or project.lower() == "math" and int(bug_id) >= 85:
                proj_source_dir = os.path.join(project_dir, "src/java")
    return proj_source_dir