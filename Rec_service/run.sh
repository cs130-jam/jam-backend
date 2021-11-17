cd Rec_service

source venv/bin/activate
if ! command -v python3 &> /dev/null
then
    python reco.py

else
    python3 reco.py
fi



