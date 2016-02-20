SKEL=$OSCA/etc/skel
echo "     source: $HOME"
echo destination: $SKEL

rm -rf $SKEL&&
mkdir $SKEL&&
cp -av $HOME/.alias $SKEL&&
cp -av $HOME/.bashrc $SKEL&&
cp -av $HOME/.gtkrc-2.0 $SKEL&&
cp -av $HOME/.xsession $SKEL&&
mkdir $SKEL/.config&&
cp -rav $HOME/.config/leafpad $SKEL/.config&&
cp -rav $HOME/.config/lxterminal $SKEL/.config&&
cp -rav $HOME/.config/Thunar $SKEL/.config&&
cp -rav $HOME/.config/vlc $SKEL/.config&&
cp -rav $HOME/.config/gtk-2.0 $SKEL/.config&&
cp -rav $HOME/.config/gtk-3.0 $SKEL/.config&&

echo done

