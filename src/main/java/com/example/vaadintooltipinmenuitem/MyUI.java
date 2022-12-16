package com.example.vaadintooltipinmenuitem;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Binder;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import java.util.Optional;

@Theme("mytheme")
public class MyUI extends UI {

	private static final long serialVersionUID = 1L;

	@Override
	protected void init(VaadinRequest vaadinRequest) {

		Object withBinderObj = vaadinRequest.getParameter("withBinder");
		boolean withBinder = false;
		try {
			withBinder = Boolean.parseBoolean(vaadinRequest.getParameter("withBinder"));
		} catch (Throwable throwable) {

		}
		final VerticalLayout layout = new VerticalLayout();

		ComboBox<String> combo = new ComboBox<>();
		combo.setNewItemProvider(string -> Optional.of(string));
		TextField textField = new TextField();

		if (withBinder) {
			DataClass data = new DataClass();

			Binder<DataClass> binder = new Binder<>(DataClass.class);
			binder.setBean(data);
			binder.forField(combo).bind(DataClass::getStringProp, (bean, value) -> bean.setStringProp(value));
			binder.forField(textField).bind(DataClass::getStringProp, (bean, value) -> bean.setStringProp(value));
			Label label = new Label("with binder");
			layout.addComponent(label);
		} else {
			Label label = new Label("without binder");
			layout.addComponent(label);
		}

		combo.addValueChangeListener(evt -> {
			if (evt.isUserOriginated() && evt.getValue() != null) {
				combo.clear();
			}
		});

		layout.addComponent(combo);
		layout.addComponent(textField);

		layout.setMargin(true);
		layout.setSpacing(true);

		setContent(layout);
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
		private static final long serialVersionUID = 1L;
	}

}
