function postRequest(url, params, fun) {
	var url1, params1, fun1;
	if (arguments.length < 2) {
		return;
	}
	if (arguments.length == 2) {
		url1 = arguments[0];
		fun1 = arguments[1];
		params1 = {};
	}
	if (arguments.length >= 3) {
		url1 = arguments[0];
		params1 = arguments[1];
		fun1 = arguments[2];
	}
	var lidx = layer.load(1, {
		shade: [0.1, '#99CCCC']
	});
	$.post(url1, params1, function(data) {
		layer.close(lidx);
		if (data && typeof data == 'string' && data.indexOf('login_page_flag_4_ajax_request_redirect') > 0) {
			window.location.reload();
			return;
		}
		if (typeof fun1 == 'function') {
			eval(fun1(data));
		}
	}).fail(function(response) {
		layer.close(lidx);
		console.log('Error: ' + response.responseText);
		layer.msg('系统故障，速速请技术人员解决：' + JSON.parse(response.responseText).status, {
			offset: 't',
			anim: 6
		});
	});
}

function getRequest(url, fun) {
	$.get(url, function(data) {
		if (data && typeof data == 'string' && data.indexOf('login_page_flag_4_ajax_request_redirect') > 0) {
			window.location.reload();
			return;
		}
		if (typeof fun == 'function') {
			eval(fun(data));
		}
	});
}

function syncRequest(url, params, fun, method) {
	var url1, params1, fun1;
	if (arguments.length < 2) {
		return;
	}
	if (arguments.length == 2) {
		url1 = arguments[0];
		fun1 = arguments[1];
		params1 = {};
	}
	if (arguments.length >= 3) {
		url1 = arguments[0];
		params1 = arguments[1];
		fun1 = arguments[2];
	}
	var lidx = layer.load(1, {
		shade: [0.1, '#99CCCC']
	});
	$.ajax({
		async: false,
		type: method,
		url: url1,
		data: params1,
		success: function(data) {
			layer.close(lidx);
			if (data && typeof data == 'string' && data.indexOf('login_page_flag_4_ajax_request_redirect') > 0) {
				window.location.reload();
				return;
			}
			if (typeof fun1 == 'function') {
				eval(fun1(data));
			}
		},
		error: function(response) {
			layer.close(lidx);
			console.log('Error: ' + response.responseText);
			layer.msg('系统故障，速速请技术人员解决：' + JSON.parse(response.responseText).status, {
				offset: 't',
				anim: 6
			});
		}
	});
}
