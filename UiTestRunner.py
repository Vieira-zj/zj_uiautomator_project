# -*- coding: utf-8 -*-

'''
Created on 2016-6-3

@author: zhengjin
'''

import os
import sys

# --------------------------------------------------------------
# Vars
# --------------------------------------------------------------
g_prj_root_path = os.getcwd()
# g_prj_root_path = r'E:\AndroidStudio_Workspace\FunSettingsUITest'
g_prj_output_path = os.path.join(g_prj_root_path, r'app\build\outputs\apk')

g_prj_output_debug_apk_name = 'app-debug.apk'
g_prj_output_debug_apk_path = os.path.join(g_prj_output_path, g_prj_output_debug_apk_name)

g_prj_output_release_apk_name = 'app-release-unsigned.apk'
g_prj_output_release_apk_path = os.path.join(g_prj_output_path, g_prj_output_release_apk_name)

g_prj_output_android_instrument_test_name = 'app-debug-androidTest-unaligned.apk'
g_prj_output_android_instrument_test_path = \
    os.path.join(g_prj_output_path, g_prj_output_android_instrument_test_name)

g_shell_data_tmp_path = r'/data/local/tmp'
g_package_name = 'com.example.zhengjin.funsettingsuitest'
g_instrument_test_name = '%s.test' %(g_package_name)
g_push_apk_file_path = '%s/%s' %(g_shell_data_tmp_path, g_package_name)
g_push_instrument_test_file_path = '%s/%s' %(g_shell_data_tmp_path, g_instrument_test_name)

g_instrument_test_runner = \
    '%s/android.support.test.runner.AndroidJUnitRunner' %(g_instrument_test_name)

# var to be update
g_test_cases = '%s.testcasedemos.TestTaskLauncher' %(g_package_name)
g_flag_build = False;
g_flag_run = True;


# --------------------------------------------------------------
# Functions, build app and instrument tests.
# --------------------------------------------------------------
def build_testing_prj():
    run_prj_clean()
    run_prj_assemble_android_test()
    run_prj_assemble_debug()
    verify_build_output_files_exist()

def run_win_command(cmd):
    print 'Run cmd:: %s' %(cmd)
    ret = os.system(cmd)
    if ret == 0:
        return
    else:
        print 'Error, exec command %s' %(cmd)
        sys.exit(1)

def run_prj_clean():
    cmd = 'gradlew clean'
    run_win_command(cmd)

def run_prj_assemble_android_test():
    cmd = 'gradlew assembleAndroidTest'
    run_win_command(cmd)

def run_prj_assemble_release():
    cmd = 'gradlew assembleRelease'
    run_win_command(cmd)

def run_prj_assemble_debug():
    cmd = 'gradlew assembleDebug'
    run_win_command(cmd)

def verify_file_exist(path):
    if os.path.exists(path):
        print 'verify file %s exist. ' %(path)
        return
    else:
        print 'The file %s is NOT exist.' %(path)
        sys.exit(1)

def verify_build_output_files_exist():
    verify_file_exist(g_prj_output_debug_apk_path)
    verify_file_exist(g_prj_output_android_instrument_test_path)


# --------------------------------------------------------------
# Functions, execute the UI test cases.
# --------------------------------------------------------------
def run_win_command_with_output(cmd):
    print 'Run cmd:: %s' %(cmd)
    output = os.popen(cmd)
    return output.readlines()

def verify_adb_connection():
    cmd = 'adb get-serialno'
    lines = run_win_command_with_output(cmd)

    if (len(lines) == 1):
        if (lines[0] == 'unknown\n'):
            print 'There is no adb connection.'
            sys.exit(1)
        else:
            print 'The adb connection: %s' %(lines[0])
            return
    elif (len(lines) > 1):
        print 'There are more than one adb connection.'
        sys.exit(1)
    else:
        print 'unknown error, checking the adb connection.'
        sys.exit(1)

def push_and_install_debug_apk_file():
    push_cmd = 'adb push'
    cmd = '%s %s %s' %(push_cmd, g_prj_output_debug_apk_path, g_push_apk_file_path)
    run_win_command(cmd)

    install_cmd = 'adb shell pm install -r'
    cmd = '%s %s' %(install_cmd, g_push_apk_file_path)
    run_win_command(cmd)

def push_and_install_testing_instrument_file():
    push_cmd = 'adb push'
    cmd = '%s %s %s' %(
        push_cmd, g_prj_output_android_instrument_test_path, g_push_instrument_test_file_path)
    run_win_command(cmd)

    install_cmd = 'adb shell pm install -r'
    cmd = '%s %s' %(install_cmd, g_push_instrument_test_file_path)
    run_win_command(cmd)

def run_instrument_test():
    instrument_cmd = 'adb shell am instrument -w -r   -e debug false -e class'
    test_cases = g_test_cases
    instrument_test_runner = g_instrument_test_runner
    cmd = '%s %s %s' %(instrument_cmd, test_cases, instrument_test_runner)

    return run_win_command_with_output(cmd)

def push_and_run_instrument_test():
    push_and_install_debug_apk_file()
    push_and_install_testing_instrument_file()
    run_instrument_test()


# --------------------------------------------------------------
# Test report
# --------------------------------------------------------------
def filter_and_format_test_output(lines):
    test_case = ''
    test_class = ''
    test_cases = []
    flag_num = True
    test_number = ''
    flag_time = True
    test_time = ''

    for line in lines:
        if 'test=' in line:
            test_case = line.split(' ')[1].strip('\n')
        if 'class=' in line:
            test_class = line.split(' ')[1].strip('\n')
            test_cases.append('%s::%s' %(test_class, test_case))
            continue
        if flag_num and ('numtests=' in line):
            test_number = line.split(' ')[1].strip('\n')
            flag_num = False
            continue
        if flag_time and ('Time:' in line):
            test_time = line.split(' ')[1].strip('\n')
            flag_time = False

    print 'Test number: %s' %(test_number)
    print 'Total test time: %s' %(test_time)
    for case in set(test_cases):
        print 'Test case: %s' %(case)
    # return test_number, test_time, set(test_cases)

def generate_xml_test_report():
    print 'TODO'

def generate_html_test_report():
    print 'TODO'


# --------------------------------------------------------------
# Main
# --------------------------------------------------------------
def test_main():
    if g_flag_build:
        build_testing_prj()

    if g_flag_run:
        push_and_run_instrument_test()


if __name__ == '__main__':

    test_main()
    print "***** %s done." %(__file__)

    pass