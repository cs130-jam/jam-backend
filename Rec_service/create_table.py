import json
import pymysql
import uuid
import random
import pandas as pd
import numpy as np
from get_db_info import getProps
from constants import genres, db_name, artists, instruments

props = getProps()
username = props.username
password = props.password

n_artists = len(artists)
n_users = 100
n_artists_per_user = 3
subsets = np.empty((n_users, n_artists_per_user), dtype=np.uint32)
for i in range(n_users):
    subsets[i] = np.random.choice(np.arange(n_artists), n_artists_per_user, replace=False)

connection = pymysql.connect(host='localhost', port=3306, user=username, passwd=password)
with connection:
    with connection.cursor() as cursor:
        cursor.execute("CREATE DATABASE IF NOT EXISTS " + db_name)
        cursor.execute("USE " + db_name)
        cursor.execute("DROP TABLE IF EXISTS user_interests")
        cursor.execute("CREATE TABLE user_interests (uid char(36) PRIMARY KEY, interests text)")

        sql = "INSERT INTO user_interests (uid, interests) VALUES (%s, %s)"
        for i, subset in enumerate(subsets):
            genresMap = {genre: 0 for genre in genres}
            for index in subset:
                for genre, count in artists[index]["styles"].items():
                    genresMap[genre] += count

            total_genre_count = sum(genresMap.values())
            if total_genre_count == 0:
                total_genre_count = 1

            normalized_genres = {genre: genresMap[genre] / total_genre_count for genre in genresMap}
            cursor.execute(sql, (str(uuid.UUID(int=i)), json.dumps(normalized_genres)))
            connection.commit()

        print(pd.read_sql_query("SELECT uid, interests FROM user_interests", connection))

connection = pymysql.connect(host='localhost', port=3306, user=username, passwd=password, database="jam")
with connection:
    with connection.cursor() as cursor:
        sqlUsers = "INSERT INTO users (id, profile, preferences) VALUES (%s, %s, %s)"
        sqlCredentials = "INSERT INTO internal_credentials (username, userId, password_hash) VALUES (%s, %s, %s)"

        for i, subset in enumerate(subsets):
            artistSubset = [artists[index] for index in subset]

            uid = str(uuid.UUID(int=i))
            profile = {
                "firstName": "User" + str(i + 1),
                "lastName": "Test",
                "bio": "",
                "location": {
                    "longitude": str(-118.4508667 + random.random() - 0.5),
                    "latitude": str(34.0687462 + random.random() - 0.5)
                },
                "pfpUrl": "",
                "musicInterests": [
                    {"id": artist["id"], "name": artist["name"], "path": "artists/" + artist["id"], "thumb": artist["thumb"]} for artist in artistSubset
                ],
                "instruments": np.random.choice(instruments, random.randint(1, 5)).tolist()}
            preferences = {"maxDistance": {"value": 50.0, "units": "Miles"}, "wantedInstruments": []}

            cursor.execute(sqlUsers, (uid, json.dumps(profile), json.dumps(preferences)))
            connection.commit()

            # below is hash of password 'password'
            cursor.execute(sqlCredentials, (profile["firstName"], uid, "$2a$10$YCObWZ1hSDYZnli4WP1Cc.1JDa4W/wDbve62j3lp1WYs6ST8EKeMq"))
            connection.commit()

