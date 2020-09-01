# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

LICENSE = "BSL-1.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e4224ccaecb14d942c71d31bef20d78c"

SRC_URI = " \
	git://github.com/TheYoctoJester/simple_echo_server.git;protocol=https \
	file://simpleechoserver.service \
"

# Modify these as desired
PV = "1.0+git${SRCPV}"
SRCREV = "670f02380fa00be3d2f83b597b2811052f1991ca"

S = "${WORKDIR}/git"

DEPENDS = "boost"

inherit cmake systemd

# systemd.bbclass tells systemd to start services while booting automatically
# via:
# SYSTEMD_AUTO_ENABLE ??= "enabled"
# if you don't want this service to start automatically while booting
# (e.g. you are not sure it's going to work)
# just set here:
# SYSTEMD_AUTO_ENABLE = "disable"

SYSTEMD_SERVICE_${PN} = "simpleechoserver.service"

do_install_append() {
	if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
		install -d ${D}${systemd_unitdir}/system
		for service in ${SYSTEMD_SERVICE_${PN}}; do
			install -m 0644 ${WORKDIR}/${service} ${D}${systemd_unitdir}/system/
			sed -i -e 's,@BINDIR@,${bindir},g' ${D}${systemd_unitdir}/system/${service}
		done
	fi
}


# Specify any options you want to pass to cmake using EXTRA_OECMAKE:
EXTRA_OECMAKE = ""

