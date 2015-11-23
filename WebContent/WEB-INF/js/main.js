var login = document.getElementById('login');
var signup = document.getElementById('signup');

function hasClass(elem, klass) {
    return (" " + elem.className + " ").indexOf(" " + klass + " ") > -1;
}

function changeForm() {
	if ('onhashchange' in window) {
		if (location.hash === '#login') {
			if (!hasClass(signup, 'hidden')) {
				signup.className = signup.className += 'hidden';
				
				if(hasClass(login, 'hidden')) {
					login.className = login.className.replace(/\hidden\b/, '');
				}
			}
			console.log('LOGIN');
		}
		if(location.hash === '#signup') {
			if (!hasClass(login, 'hidden')) {
				login.className = login.className += 'hidden';
				
				if(hasClass(signup, 'hidden')) {
					signup.className = signup.className.replace(/\hidden\b/, '');
				}
			}	
			console.log('SIGNUP');
		}
	}
}

window.onhashchange = changeForm;

var switches = document.getElementsByClassName('form-switch');
[].forEach.call(switches, function(e) {
	e.addEventListener('click', function() {
		if (hasClass(e, 'login')) {
			hasClass(signup, 'hidden') ? '' : signup.className += ' hidden';
			login.className = login.className.replace(/\hidden\b/,'');
			
		} else {
			hasClass(login, 'hidden') ? '' : login.className += ' hidden';
			signup.className = signup.className.replace(/\hidden\b/,'');
		}
		e.style.color = '#e74c3c';
	});
});

