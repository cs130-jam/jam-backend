cd Rec_service
if ! command -v python3 &> /dev/null
then
    python -m venv venv

else
    python3 -m venv venv
fi
source venv/bin/activate
pip install -r requirements.txt
python create_table.py
