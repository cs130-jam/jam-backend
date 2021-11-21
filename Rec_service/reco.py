from __future__ import annotations
import json
import pymysql
import pandas as pd
from math import ceil
from collections import defaultdict
from dataclasses import dataclass
from sklearn.neighbors import NearestNeighbors
from flask import Flask, request, jsonify
from get_db_info import getProps
from constants import genres, db_name

props = getProps()
username = props.username
password = props.password

connection = pymysql.connect(host='localhost', port=int(3306), user=username, passwd=password, db=db_name)

# create the Flask app
app = Flask(__name__)


@dataclass
class InsertRequestBody:
    uid: str
    genres: dict[str, int]


@dataclass
class GetMatchesResponse:
    totalPages: int
    users: list[str]


@app.route('/insert_user', methods=['POST'])
def insert_user():
    request_body = InsertRequestBody(**request.get_json())
    total_genre_count = sum(request_body.genres.values())
    filled_genres = defaultdict(request_body.genres, 0)
    normalized_genres = {genre: filled_genres[genre] / total_genre_count for genre in genres}
    sql = "INSERT INTO user_interests (uid, interests) VALUES (%s, %s) ON DUPLICATE KEY UPDATE interests=%s"
    genres_json = json.dumps(normalized_genres)
    with connection.cursor() as cursor:
        cursor.execute(sql, (genres_json, request_body.uid, genres_json))

    connection.commit()
    return '', 204


@app.route('/get_match', methods=['GET'])
def get_match():
    page_size = 50
    page_index = int(request.args.get("page"))
    uid = request.args.get("uid")

    user_interests = get_user_interests(uid)
    if user_interests is None:
        return GetMatchesResponse(0, []).__dict__, 200

    dfsql = pd.read_sql_query("SELECT uid, interests FROM user_interests WHERE uid!=%s", connection, params=[uid])
    genres_table = pd.DataFrame(dfsql["interests"].apply(json.loads).tolist())
    knn = NearestNeighbors(n_neighbors=genres_table.shape[0]).fit(genres_table)

    distances, indices = knn.kneighbors(user_interests)
    page_list = indices.tolist()[0][page_index*page_size:page_index*page_size + page_size]
    total_pages = ceil(dfsql.shape[0] / page_size)
    return GetMatchesResponse(total_pages, (dfsql.iloc[page_list, :]['uid']).tolist()).__dict__


def get_user_interests(uid):
    with connection.cursor() as cursor:
        sql_query = "SELECT interests FROM user_interests WHERE uid=%s"
        cursor.execute(sql_query, uid)
        user_interests = cursor.fetchone()
        if user_interests is None:
            return None

        user_interests_table = pd.DataFrame([json.loads(user_interests[0])])
        return user_interests_table


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
