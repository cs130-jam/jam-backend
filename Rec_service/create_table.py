import json
import pymysql
import uuid
import pandas as pd
import numpy as np
from get_db_info import getProps
from constants import genres, db_name

props = getProps()
username = props.username
password = props.password

connection = pymysql.connect(host='localhost', port=int(3306), user=username, passwd=password)
with connection.cursor() as cursor:
    cursor.execute("CREATE DATABASE IF NOT EXISTS " + db_name)
    cursor.execute("USE " + db_name)
    cursor.execute("CREATE TABLE IF NOT EXISTS user_interests (uid char(36) PRIMARY KEY, interests text)")

    df = pd.DataFrame(np.random.randint(0, 20, size=(100, len(genres))), columns=genres)
    df = df.div(df.sum(axis=1), axis=0)

    genres_table = [dict(v) for _, v in df.iterrows()]
    sql = "INSERT INTO user_interests (uid, interests) VALUES (%s, %s)"
    for index, genres in enumerate(genres_table):
        cursor.execute(sql, (str(uuid.UUID(int=index)), json.dumps(genres)))
        connection.commit()

    print(pd.read_sql_query("SELECT uid, interests FROM user_interests", connection))