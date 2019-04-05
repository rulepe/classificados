$(function() {
	var settings = {
			type: 'json',
			method: 'POST',
			url: '/fotos',
			multiple: true,
			concurrent: 5,
			allow: '*.(jpg|jpeg|png)',
			complete: function(resposta) {
				
				var inputImagens = $('input[name=imagens]');
				
				var inputNomeFoto = $('input[name=foto]');
				
				var inputContentType = $('input[name=contentType]');
				
				var htmlFotoClassificadosTemplate = $('#foto-classificados').html();
				var template = Handlebars.compile(htmlFotoClassificadosTemplate);
				var htmlFotoClassificados = template({nomeFoto: resposta.response.nome});
				
				var containerFotoClassificados = $('.js-container-foto-classificados');
				
									
				inputNomeFoto.val(resposta.response.nome);
				inputContentType.val(resposta.response.contentType);
				
				$('#upload-drop').hide();
				inputImagens.val();
				
				containerFotoClassificados.append(htmlFotoClassificados);
				
				$('.js-remove-foto').on('click', function() {
					$('.js-foto-classificados').hide();
					$('#upload-drop').show();
					inputNomeFoto.val('');
					inputContentType.val('');
				});
			}
	};
				
	UIkit.upload($('.js-upload'), settings);
				
});