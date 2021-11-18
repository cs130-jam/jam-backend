cd Rec_service
if (-not $?) {
    throw 'Wrong directory'
}

if (Get-Command python3) {
    python3 -m venv venv
} else {
    python -m venv venv
}

& "venv\bin\Activate.ps1"
pip install -r requirements.txt
python create_table.py
cd ..
deactivate