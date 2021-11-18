@echo off
fltmc >nul 2>&1 || (
    echo Error: this script must be run as admin
    exit
)
cd Rec_service

py -3.8 -m venv venv
call venv\Scripts\activate.bat || exit
python -m pip install --upgrade pip
pip install -r requirements.txt
python create_table.py
deactivate