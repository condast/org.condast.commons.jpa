package org.condast.commons.jpa.authentication.ui.def;

import org.eclipse.swt.graphics.Image;
import org.condast.commons.data.user.ILoginUser;
import org.condast.commons.jpa.authentication.core.IAuthenticationListener;
import org.condast.commons.jpa.authentication.core.IAuthenticationManager;
import org.condast.commons.jpa.authentication.core.IAuthenticationManager.AuthenticationResults;
import org.eclipse.swt.widgets.Text;

@Deprecated
public interface IAuthenticationControl {

	Image getImage();

	void setImage(Image image);

	void addListener(IAuthenticationListener listener);

	void removeListener(IAuthenticationListener listener);

	void setManager(IAuthenticationManager<ILoginUser> manager);

	/**
	 * Set the result message
	 * @param result
	 */
	void setResultMessage(AuthenticationResults result, String info);

	Text getPasswordWidget();

	Text getNameWidget();

}