SKEL=$OSCA/etc/skel
echo "     source: $HOME"
echo destination: $SKEL

rm -rf $SKEL&&
mkdir $SKEL&&
cp -av $HOME/.profile $SKEL&&
cp -av $HOME/.bashrc $SKEL&&
cp -av $HOME/.gtkrc-2.0 $SKEL&&
cp -av $HOME/.xinitrc $SKEL&&
cp -av $HOME/.nanorc $SKEL&&
mkdir $SKEL/.config&&
cp -rav $HOME/.config/leafpad $SKEL/.config&&
cp -rav $HOME/.config/libfm $SKEL/.config&&
cp -rav $HOME/.config/pcmanfm $SKEL/.config&&
cp -rav $HOME/.config/gtk-2.0 $SKEL/.config&&

echo done

