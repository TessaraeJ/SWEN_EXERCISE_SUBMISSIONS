#!flask/bin/python
from flask import Flask
from flask import abort, jsonify, make_response, request

app = Flask(__name__)

#task array format
tasks = [
	{
		'id': 1,
		'title': u'Buy groceries',
		'description': u'Milk, Cheese, Pizza, Fruit, Tylenol',
		'done': False
	},
	{
		'id': 2,
		'title': u'Learn Python',
		'description': u'Need to find a good Python tutorial on the web',
		'done': False
	}
]

#user array format
users = [
    {
        'userID': 1,
        'username':u'thomas',
        'password':u'123456'
    },
    {
        'userID':2,
        'username':u'jane',
        'password':u'234567'
    }
]

@app.route('/todo/api/v1.0/tasks', methods=['GET'])
def get_tasks():
	return jsonify({'tasks': tasks})

@app.route('/todo/api/v1.0/tasks/<int:task_id>', methods=['GET'])
def get_task(task_id):
	task = [task for task in tasks if task['id'] == task_id]
	if len(task) == 0:
		abort(404)
	return jsonify({'task': task[0]})

@app.errorhandler(404)
def not_found(error):
	return make_response(jsonify({'error': 'Not found'}), 404)

@app.route('/todo/api/v1.0/tasks', methods=['POST'])
def create_task():
	if not request.json or not 'title' in request.json:
		abort(400)
	task = {
		'id': tasks[-1]['id'] + 1,
		'title': request.json['title'],
		'description': request.json.get('description', ""),
		'done': False
	}
	tasks.append(task)
	return jsonify({'task': task}), 201

@app.route('/todo/api/v1.0/tasks/<int:task_id>', methods=['PUT'])
def update_task(task_id):
	task = [task for task in tasks if task['id'] == task_id]
	if len(task) == 0:
		abort(404)
	if not request.json:
		abort(400)
	if 'title' in request.json and type(request.json['title']) != unicode:
		abort(400)
	if 'description' in request.json and type(request.json['description']) is not unicode:
		abort(400)
	if 'done' in request.json and type(request.json['done']) is not bool:
		abort(400)
	task[0]['title'] = request.json.get('title', task[0]['title'])
	task[0]['description'] = request.json.get('description', task[0]['description'])
	task[0]['done'] = request.json.get('done', task[0]['done'])
	return jsonify({'task': task[0]})

@app.route('/todo/api/v1.0/tasks/<int:task_id>', methods=['DELETE'])
def delete_task(task_id):
	task = [task for task in tasks if task['id'] == task_id]
	if len(task) == 0:
		abort(404)
	tasks.remove(task[0])
	return jsonify({'result': True})

#function to return all users
@app.route('/todo/api/v1.0/users', methods=['GET'])
def get_users():
	return jsonify({'users': users})

#function to login a user
@app.route('/todo/api/v1.0/users/<int:user_id>', methods=['GET'])
def get_user(user_id):
    user = [user for user in users if user['userID'] == user_id]
    if len(user) == 0:
        abort(404)
    return jsonify({'user': user[0]})

#function to register a user
@app.route('/todo/api/v1.0/users', methods=['POST'])
def create_user():
	if not request.json or not 'username' in request.json:
		abort(400)
	user = {
		'id': users[-1]['id'] + 1,
		'username': request.json['username'],
		'password': request.json.get('password'),
		'done': False
	}
	users.append(user)
	return jsonify({'user': user}), 201

if __name__ == '__main__':
	app.run(host='0.0.0.0', debug=True)
