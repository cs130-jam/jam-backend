cd Rec_service
if (-not $?) {
    throw 'Wrong directory'
}

& 'venv\bin\Activate.ps1'
python reco.py
cd ..
deactivate