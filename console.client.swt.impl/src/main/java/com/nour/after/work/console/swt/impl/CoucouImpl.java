package com.nour.after.work.console.swt.impl;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import com.nour.after.work.console.swt.api.CoucouApi;

@Component(name="coucouSwt", immediate=true)
public class CoucouImpl implements CoucouApi {


	@Activate
	public void start(BundleContext context) throws Exception {
		System.out.println(this.getClass().getName() + " activated");
		coucou();
	}
	
	public void coucou() throws Exception {
		System.out.println(COUCOU_WORD + " mon " + COCU_WORD);
	}

	@Deactivate
	public void stop(BundleContext context) throws Exception {
		System.out.println(this.getClass().getName() + " Stopping");
	}
}
