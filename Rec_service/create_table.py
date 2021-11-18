import json
import pymysql
import pandas as pd
import numpy as np
from get_db_info import getProps
from constants import genres, db_name

props = getProps()
username = props.username
password = props.password

connection=pymysql.connect(host='localhost', port=int(3306), user=username, passwd=password)
mycursor = connection.cursor()
mycursor.execute("CREATE DATABASE IF NOT EXISTS " + db_name)
mycursor.execute("USE " + db_name)
mycursor.execute("CREATE TABLE IF NOT EXISTS user_interests (uid char(36), interests text)")
# In[ ]:
df = pd.DataFrame(np.random.randint(0,20,size=(100, 584)), columns= genres)
df = df.div(df.sum(axis=1), axis=0)
df['uid'] = df.index
first_column = df.pop('uid')

# insert column using insert(position,column_name,
# first_column) function
df.insert(0, 'uid', first_column)
df['uid'] = df.uid.apply(str)
# In[ ]:
k = [dict(v) for _, v in df.iterrows()]
# In[ ]:
#mycursor.execute("CREATE TABLE user_interests (uid char(36), interests text)")
sql = "INSERT INTO user_interests (uid, interests) VALUES (%s, %s)"
# In[ ]:
for K in k:
    dic = K
    val = (str(dic['uid']),json.dumps(dic))
    mycursor.execute(sql, val)
    connection.commit()
# In[ ]:
dfsql=pd.read_sql_query("SELECT uid, interests FROM user_interests ",connection)
print(dfsql)