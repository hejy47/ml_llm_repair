import os
import unidiff

def extract_buggy_line_from_diff_file(diff_file, project_source_dir):
    modified_lines = []
    patch = unidiff.PatchSet.from_filename(diff_file, encoding="utf-8", errors="ignore")
    for patch_file in patch:
        patch_file_name = patch_file.source_file.replace(project_source_dir, "")
        tmp_del_lines = []
        tmp_add_lines = []
        add_count = 0
        for patch_file_hunk in patch_file:
            for patch_file_line in patch_file_hunk:
                if patch_file_line.is_removed:
                    tmp_del_lines.append(patch_file_line.source_line_no)
                elif patch_file_line.is_added:
                    tmp_add_lines.append(patch_file_line.target_line_no - add_count)
                    add_count += 1
        tmp_modified_lines = set(tmp_del_lines)
        for tmp_add_line in tmp_add_lines:
            tmp_modified_lines.add(tmp_add_line)
            if tmp_add_line not in tmp_del_lines:
                tmp_modified_lines.add(tmp_add_line-1)
        tmp_modified_lines = list(sorted(tmp_modified_lines))
        for tmp_modified_line in tmp_modified_lines:
            modified_lines.append("{}#{}".format(patch_file_name[:-5].replace('/','.'), tmp_modified_line))
    return modified_lines

if __name__ == "__main__":
    diff_dir = "../../d4j-human-patch"
    output_dir = "../location/defects4j/manual"
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
            diff_path = os.path.join(diff_dir, "{}_{}.diff".format(project.lower(), bug_id))

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
            # project_source_dir = "/mnt/data/d4j-v1.4.0/{}_{}_buggy/{}/".format(project.lower(), bug_id, source_dir)
            project_source_dir = "d4j-v2.0/{}/buggy_version/{}_{}/{}/".format(project, project.lower(), bug_id, source_dir)
            if project in d4jv1_info and bug_id in d4jv1_info[project]:
                project_source_dir = "d4j-v1.4/{}/buggy_version/{}_{}/{}/".format(project, project.lower(), bug_id, source_dir)

            modified_lines = extract_buggy_line_from_diff_file(diff_path, project_source_dir)

            location_path = os.path.join(output_dir, project.lower(), "{}.txt".format(bug_id))
            if not os.path.exists(os.path.dirname(location_path)):
                os.makedirs(os.path.dirname(location_path))
            with open(location_path, "w") as f:
                f.write("\n".join(modified_lines))