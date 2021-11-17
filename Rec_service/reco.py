import json
import pymysql
import pandas as pd
from sklearn.neighbors import NearestNeighbors
from flask import Flask, request, jsonify
from get_db_info import getProps
from genres import genres

props = getProps()
username = props.username
password = props.password

connection = pymysql.connect(host='localhost', port=int(3306), user=username, passwd=password, db='employee_management_system')
mycursor = connection.cursor()

# create the Flask app
app = Flask(__name__)


@app.route('/query-example')
def query_example():
    return 'Query String Example'


@app.route('/form-example')
def form_example():
    return 'Form Data Example'


@app.route('/json-example', methods=['POST'])
def json_example():
    page_index = 0

    dfsql=pd.read_sql_query("SELECT uid, interests FROM user_interests ",connection)
    dfsql['interests'] = dfsql['interests'].apply(lambda x:json.loads(x))
    tmp = pd.DataFrame(columns = ['uid'] + genres)
    l = dfsql['interests'].tolist()
    tmp = tmp.append(l, ignore_index=True)
    knn = NearestNeighbors(n_neighbors = dfsql.shape[0]).fit(tmp[genres])
    d = request.get_json()
    e = pd.DataFrame(columns = genres)
    e = e.append(d, ignore_index=True)
    e = e.drop(columns = ['uid'])
    e = e.fillna(0)
    e = e.div(e.sum(axis=1), axis=0)
    e['uid'] = d['uid']
    first_column = e.pop('uid')
    e.insert(0, 'uid', first_column)
    distances, indicies = knn.kneighbors(e[genres])
    l = indicies.tolist()
    page_list = l[0][page_index*50:page_index*50 + 50]
    listtoreturn = (dfsql.iloc[page_list, :]['uid']).tolist()
    return jsonify(listtoreturn)


if __name__ == '__main__':
    # run app in debug mode on port 5000
    app.run(host='0.0.0.0', port=105)
