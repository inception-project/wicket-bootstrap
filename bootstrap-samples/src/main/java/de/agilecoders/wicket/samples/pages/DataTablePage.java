package de.agilecoders.wicket.samples.pages;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.filter.BootstrapChoiceFilteredPropertyColumn;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.filter.BootstrapSelectFilter;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.filter.BootstrapTextFilteredPropertyColumn;
import de.agilecoders.wicket.samples.dataprovider.ExampleFilterSortDataProvider;
import de.agilecoders.wicket.samples.dataprovider.ExampleFilterSortDataProvider.ExampleFilter;
import de.agilecoders.wicket.samples.dataprovider.ExampleFilterSortDataProvider.ExampleFilterSortRowItem;
import de.agilecoders.wicket.samples.dataprovider.ExampleFilterSortDataProvider.GenderType;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterToolbar;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A page to show the use of bootstrap datatables.
 *
 * @author drummer
 */
@MountPath(value = "/datatable")
public class DataTablePage extends BasePage {

    private static final int ROWS_PER_PAGE = 10;

    /**
     * Construct.
     *
     * @param parameters current page parameters
     */
    public DataTablePage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        ExampleFilterSortDataProvider dataProvider = new ExampleFilterSortDataProvider();
        final BootstrapDefaultDataTable dataTable = createDataTable(dataProvider);
        final FilterForm<ExampleFilter> filterForm = new FilterForm<>("filterForm", dataProvider);
        filterForm.add(dataTable);
        dataTable.addTopToolbar(new FilterToolbar(dataTable, filterForm)); // makes the filters visible in top of every column
        add(filterForm);
    }

    private BootstrapDefaultDataTable<ExampleFilterSortRowItem, String> createDataTable(final ExampleFilterSortDataProvider dataProvider) {
        return new BootstrapDefaultDataTable<>("dataTable", createColumns(), dataProvider, ROWS_PER_PAGE).striped().bordered().condensed();
    }

    private List<IColumn<ExampleFilterSortRowItem, String>> createColumns() {

        final List<IColumn<ExampleFilterSortRowItem, String>> columns = new ArrayList<>();

        columns.add(new BootstrapTextFilteredPropertyColumn<>(Model.of("First Name"), ExampleFilterSortRowItem.SORTPARAM_FIRST_NAME,
            ExampleFilterSortRowItem.PROPERTY_FIRST_NAME, ExampleFilter.FILTER_FIRST_NAME));
        columns.add(new BootstrapTextFilteredPropertyColumn<>(Model.of("Last Name"), ExampleFilterSortRowItem.SORTPARAM_LAST_NAME,
            ExampleFilterSortRowItem.PROPERTY_LAST_NAME, ExampleFilter.FILTER_LAST_NAME));
        columns.add(new BootstrapChoiceFilteredPropertyColumn<ExampleFilterSortRowItem, GenderType, String>(Model.of("Gender"),
            ExampleFilterSortRowItem.PROPERTY_GENDER, ExampleFilterSortRowItem.SORTPARAM_GENDER,
            Model.ofList(Arrays.asList(GenderType.values())), ExampleFilter.FILTER_GENDER_TYPE) {

            @Override
            public void populateItem(Item<ICellPopulator<ExampleFilterSortRowItem>> item, String componentId, IModel<ExampleFilterSortRowItem> rowModel) {
                item.add(new Label(componentId, Model.of(rowModel.getObject().gender.getDisplayValue())));
            }

            @Override
            protected IChoiceRenderer<GenderType> getChoiceRenderer() {
                return new ChoiceRenderer<GenderType>() {

                    @Override
                    public Object getDisplayValue(GenderType object) {
                        return object.getDisplayValue();
                    }
                };
            }

            @Override
            public Component getFilter(String componentId, FilterForm<?> form) {
                final Component component = super.getFilter(componentId, form);
                if (component instanceof BootstrapSelectFilter) {
                    final BootstrapSelectFilter selectFilter = (BootstrapSelectFilter) component;
                    // change none selected to an empty string
                    selectFilter.getChoice().config().withNoneSelectedText("");
                }
                return component;
            }
        });

        return columns;
    }
}
