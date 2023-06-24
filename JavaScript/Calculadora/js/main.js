// ###########################################
// ###### DESAFIO CALCULADORA JS #############
// ###########################################

// CAPÍTULO: JAVA SCRIPT
// MÓDULO 09
// ALUNO: GUTEMBERGUE SR JUNIOR.

//ARMAZENAMENTO DE ELEMENTOS:
const inputNumber1 = document.querySelector('#number1');
const inputNumber2 = document.querySelector('#number2');
const result = document.querySelector('#result-box');
const btnPlus = document.querySelector('#btn-plus');
const btnTimes = document.querySelector('#btn-times');
const btnClear = document.querySelector('#btn-clear');
const inputError = document.querySelector('.input-error');

//CONFIGURAÇÕES DOS EVENTOS:
btnPlus.addEventListener('click', handleBtnPlusClick);
btnTimes.addEventListener('click', handleBtnTimesClick);
btnClear.addEventListener('click', handleBtnClearClick);
inputError.classList.remove('input-error');

//OPÇÃO 1 - CONTROLE DO DOS VALORES DE ENTRADA DOS INPUTS BLOQUEANDO-OS DIRETAMENTE ATRAVÉS
// DA FUNÇÃO: ***onlyNumber***, CASO O VALOR INSERIDO NÃO SEJA NÚMERO.

inputNumber1.addEventListener('keypress', onlyNumber);
inputNumber2.addEventListener('keypress', onlyNumber);

/*
OPÇÃO 2 - CONTROLE DOS VALORES DE ENTRADA DOS INPUTS HABILITANDO A CLASSE: INPUT ERROR PARA 
// VALORES DIFERENTES DE NÚMEROS.

inputNumber1.addEventListener('change', (event) => {
	if (isNaN(Number(inputNumber1.value))) {
		inputNumber1.classList.add('input-error');
	} else {
		inputNumber1.classList.remove('input-error');
	}
});

inputNumber2.addEventListener('change', (event) => {
	if (isNaN(Number(inputNumber2.value))) {
		inputNumber2.classList.add('input-error');
	} else {
		inputNumber2.classList.remove('input-error');
	}
});
*/

//FUNÇÕES:
function handleBtnPlusClick(event) {
	event.preventDefault();
	const plus = Number(inputNumber1.value) + Number(inputNumber2.value);
	result.innerHTML = plus;
	const resultPlus = result.innerHTML;

	if (isNaN(resultPlus)) {
		result.innerHTML = 0;
	}
}

function handleBtnTimesClick(event) {
	event.preventDefault();
	const times = Number(inputNumber1.value) * Number(inputNumber2.value);
	result.innerHTML = times;
	const resultTimes = result.innerHTML;
	if (isNaN(resultTimes)) {
		result.innerHTML = 0;
	}
}

function handleBtnClearClick(event) {
	inputNumber1.value = ' ';
	inputNumber2.value = ' ';
	result.innerHTML = 0;
	inputNumber1.classList.remove('input-error');
	inputNumber2.classList.remove('input-error');
}

function onlyNumber(evt) {  // [1]
	let theEvent = evt || window.event;
	let key = theEvent.keyCode || theEvent.which;
	key = String.fromCharCode(key); //[2]
	let regex = /^[0-9.]+$/; //[3]
	if (!regex.test(key)) {  //[4]
	  theEvent.returnValue = false;
	  if (theEvent.preventDefault) theEvent.preventDefault();
	}
}




