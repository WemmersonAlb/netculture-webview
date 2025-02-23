const menuProfileButton = document.getElementById('menuProfile');
const menuCreateStoreButton = document.getElementById('menuCreateStore');

menuProfileButton.addEventListener('click', () => {
    if(cLogado){
        window.location.href = '/comprador/perfil/view';
    }else if(vLogado){      
        window.location.href = '/vendedor/perfil/view';
    } else {
        window.location.href = '/index';
    }
});

if(vLogado){
    menuCreateStoreButton.addEventListener('click', () => {
        window.location.href = '/vendedor/loja/cadastro';
    });
}
