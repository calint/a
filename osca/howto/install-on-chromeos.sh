# install base system and change root
#sudo sh crouton -r sid -t extension,xorg
#sudo enter-chroot -n sid

# update and upgrade
sudo apt-get update
sudo apt-get -y upgrade

# install git and get osca
sudo apt-get -y install git
mkdir ~/w/a
git clone https://github.com/calint/a.git ~/w/a

# copy init files from osca
cp -a ~/w/a/osca/etc/skel/. ~/.

# init shell
cd && source .bashrc

# build frameless window manager
cd ~/w/a/osca/frameless/
sh make.sh

# build clonky system overview
sudo apt-get -y install libxft-dev
cd ~/w/a/osca/clonky/
sh make.sh 

# build launcher
cd ~/w/a/osca/menuq/
sh make.sh

# install iostat, image viewer, screenshot taker, editor, cli, filemanager, icon theme
sudo apt-get -y install sysstat feh scrot leafpad lxterminal thunar vlc faenza-icon-theme

# install browsers
#sudo apt-get -y install chromium-browser firefox

# install htop and killall
#sudo apt-get -y install htop sysvinit-utils

# start x11
xinit .xsession

# exit chroot
#exit

# re-enter chroot activating the new .bashrc 
#sudo enter-chroot -n sid

# notes
# incase x11 does not start check
# https://github.com/dnschneid/crouton/issues/2426
# in crosh do:
#   shell
#   sudo rm -f /dev/dri/card0
#   sudo mv /dev/dri/card{1,0}
