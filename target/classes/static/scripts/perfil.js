const menuProfileButton = document.getElementById('menuProfile');
const menuEditButton = document.getElementById('menuEdit');
const menuLogoutButton = document.getElementById('menuLogout');
const userInfo = document.getElementById('userInfo');
const editForm = document.getElementById('editForm');
const cancelEditButton = document.getElementById('cancelEdit');
const saveChangesButton = document.getElementById('saveChanges');

menuProfileButton.addEventListener('click', () => {
    userInfo.style.display = 'block';
    editForm.style.display = 'none';
});

menuEditButton.addEventListener('click', () => {
    editForm.style.display = 'block';
    userInfo.style.display = 'none';
});

menuLogoutButton.addEventListener('click', () => {
    alert('Você foi deslogado!');
    if(cLogado){
        window.location.href = '/comprador/logout';
    }else if(vLogado){
        window.location.href = '/vendedor/logout';
    }else{
        window.location.href = '/login/view';
    }
});

saveChangesButton.addEventListener('click', () => {
    editForm.style.display = 'none';
    userInfo.style.display = 'block';
});

cancelEditButton.addEventListener('click', () => {
    editForm.style.display = 'none';
    userInfo.style.display = 'block';
});