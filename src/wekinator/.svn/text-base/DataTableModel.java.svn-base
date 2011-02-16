/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wekinator;

import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import wekinator.Plog.*;

/**
 *
 * @author rebecca
 */
public class DataTableModel extends AbstractTableModel {

    private String[] columnNames;
    int numMetaData, numFeats, numParams;
    SimpleDataset dataset;

    public DataTableModel(SimpleDataset dataset) {
        this.dataset = dataset;
        this.numFeats = dataset.getNumFeatures();
        this.numParams = dataset.getNumParameters();
        this.numMetaData = 3; //for now, ID, time, & training round
        setColNames();
        dataset.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                fireTableDataChanged(); //TODO for efficiency, update this...
            }
        });
    }

    protected void setColNames() {
        columnNames = new String[numMetaData + numFeats + numParams];
        columnNames[0] = "ID";
        columnNames[1] = "Time";
        columnNames[2] = "Recording round";

        for (int i = 0; i < numFeats; i++) {
            columnNames[i + numMetaData] = dataset.getFeatureName(i);
        }
        for (int i = 0; i < numParams; i++) {
            columnNames[numMetaData + numFeats + i] = dataset.getParameterName(i);
        }
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return dataset.getNumDatapoints(); //TODO TODO TODO this is 0 for some reason!
    }

    public void addRow() {

        double[] features = new double[numFeats];
        double[] params = new double[numParams];
        boolean[] mask = new boolean[numParams];
        dataset.addInstance(features, params, mask, new Date());
        int row = dataset.getNumDatapoints();

        fireTableRowsInserted(row, row);
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        if (row >= dataset.getNumDatapoints()) {
            return null;
        }

        if (col == 0) {
            return dataset.getID(row);
        } else if (col == 1) {
            return dataset.dateDoubleToString(dataset.getTimestamp(row));
        } else if (col == 2) {
            return dataset.getTrainingRound(row);
        } else if (col < numMetaData + numFeats) {
            return dataset.getFeature(row, (col - numMetaData));
        } else if (col < numMetaData + numFeats + numParams) {
            //Treat as strings to represent missing values!
            Double d = dataset.getParam(row, col - numMetaData - numFeats);
            if (d.isNaN()) {
                return "?";
            } else {
                return Double.toString(d);
            }
        } else {
            return null;
        }
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    @Override
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        if (col >= 1) { //don't allow editing of ID
            return true;
        } else {
            return false;
        }
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {
        if (col == 0) {
            //shouldn't be editing this: it's an ID
            System.out.println("Error: shouldn't edit this cell");
            return;
        }

        //TODO: check that this value is legal!
        if (col == 1) {
            //Timestamp
            if (value instanceof String) {
                try {
                    dataset.setTimestamp(row, (String) value);
                } catch (ParseException ex) {
                    //TODO
                    Logger.getLogger(DataTableModel.class.getName()).log(Level.SEVERE, null, ex);
                    return;
                }
            } else {
                //TODO: return or set back to 1st val?
                System.out.println("Bad");
                return;
            }

        } else if (col == 2) {
            //Training round
            if (value instanceof Integer) {
                dataset.setTrainingRound(row, (Integer) value);
            } else {
                //TODO
                System.out.println("Bad");
                return;
            }

        } else if (col < numMetaData + numFeats) {
            //Assume all double feats for now
            if (value instanceof Double) {

                dataset.setFeatureValue(row, (col - numMetaData), (Double) value);
            } else {
                System.out.println("Uh oh");
                return;
            }

        } else if (col < numMetaData + numFeats + numParams) {
            //Check if legal? Probably should.
            int paramNum = col - numMetaData - numFeats;
            double d = 0;


            if (value instanceof Integer) {
                d = ((Integer) value).intValue();
            } else if (value instanceof Double) {
                d = ((Double) value).doubleValue();
            } else if (value instanceof String) {
                String s = (String) value;
                if (s.equals("?")) {
                    dataset.setParameterMissing(row, paramNum);
                    fireTableCellUpdated(row, col);
                    return; //TODO: clean up position logic here

                } else {
                    try {
                        d = Double.parseDouble((String) value);
                    } catch (Exception ex) {
                        System.out.println("BAD!"); //TODO
                        return;
                    }
                }
            }

            if (dataset.isParameterDiscrete(paramNum)) {
                if (d >= 0 && d <= dataset.maxLegalDiscreteParamValue(paramNum)) {
                    dataset.setParameterValue(row, paramNum, d);
                } else {
                    //TODO: can I erase this?
                    System.out.println("Bad value!");
                    return;
                }

            } else {
                dataset.setParameterValue(row, paramNum, d);
            }
        }

        // data[row][col] = value;
        if (WekinatorRunner.isLogging()) {
            Plog.log(Msg.DATA_VIEWER_DATA_EDITED, "row=" + row + ",col=" + col);
        }
        fireTableCellUpdated(row, col);
    }

    void deleteRows(int[] selectedRows) {
        for (int j = selectedRows.length - 1; j >= 0; j--) {

            //Delete the weka representation
            System.out.println("Trying to delete row " + selectedRows[j]);

            dataset.deleteInstance(selectedRows[j]);

            fireTableRowsDeleted(selectedRows[j], selectedRows[j]);
        }

    }

    double[] getSelectedParams(int row) {
        double f[] = new double[numParams];
        for (int i = 0; i < numParams; i++) {
            // f[i] = (float) instances[i].instance(row).classValue();
            f[i] = dataset.getParam(row, i);
            Double d = new Double(f[i]);
            if (d.isNaN()) {
                System.out.println("Error: d NaN here");
            }
        }
        return f;
    }
}
