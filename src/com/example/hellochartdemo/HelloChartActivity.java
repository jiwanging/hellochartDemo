package com.example.hellochartdemo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.formatter.ColumnChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleColumnChartValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HelloChartActivity extends Activity implements OnClickListener{

//20200201
	private Button btn_addData;
	private ColumnChartData mColumnChartData;//存数据
	private ColumnChartView mColumnChartView;//chart实例

	private List<Column> columnList = new ArrayList<Column>(); //柱子列表
	private List<AxisValue> axisValues = new ArrayList<AxisValue>();
	private ArrayList<Float> xValues = new ArrayList<Float>();//x轴数据
	private ArrayList<String> xLabel = new ArrayList<String>();//x轴标签
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hello_chart);
		
		mColumnChartView = (lecho.lib.hellocharts.view.ColumnChartView)
				findViewById(R.id.hellochart);
		btn_addData = (Button) findViewById(R.id.btn_addData);
		btn_addData.setOnClickListener(this);
		initData();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hello_chart, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 描述：按钮监听事件方法
	 */

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.btn_addData:
				//添加一个柱子的方法
				addcolumn(Float.toString((float)Math.random()*100));
				break;
		default:
			break;
		}
	}

	//初始化数据的方法
	private void initData() {
	   	 //初始化标签值
	       SimpleDateFormat sDateFormat = new SimpleDateFormat("HH:mm:ss");
			String label = sDateFormat.format(new Date());
			int num = 6;//初始化数据条数
			for(int i=0;i<num;i++){
				xLabel.add(label);//x轴标签
				xValues.add(0.00f);//Y轴的值
			}
			firstHelloChart();
	   }
	
	//第一堆柱子
	private void firstHelloChart() {
        int columnNum = xValues.size();//获取柱子个数
        //给每个柱子，以及子柱进行初始化
        for (int i = 0; i < columnNum; i++) {
        	List<SubcolumnValue> subcolumnValueList = new ArrayList<SubcolumnValue>();//保存子柱子相关值
            subcolumnValueList.add(new SubcolumnValue((float) xValues.get(i),ChartUtils.COLOR_GREEN));//产生子柱子的值以及颜色
            Column column = new Column(subcolumnValueList);//新建一个柱子，并将子柱子进行添加
            
            columnList.add(column);//在柱子列表中添加柱子
            axisValues.add(new AxisValue(i).setLabel(xLabel.get(i)));//给该柱子添加相关的坐标轴数据
            column.setHasLabels(true);//设置柱体标签
            ColumnChartValueFormatter formatter = new SimpleColumnChartValueFormatter(2);//设置小数位个数
            column.setFormatter(formatter);
            column.setHasLabelsOnlyForSelected(false);//固定显示，而非在得到点击后才进行显示
        }
        mColumnChartData = new ColumnChartData(columnList);//生成柱子的数据实例
        setColumnConfig(columnNum);
	}

	//设置图表的相关配置信息
	private void setColumnConfig(int currentNum) {
    	/*===== 坐标轴相关设置 =====*/
        Axis axisX = new Axis(axisValues);//声明X轴实例
        Axis axisY = new Axis();////声明Y轴实例
        
        //初始化X轴
        axisX.hasTiltedLabels();//设置斜体标签
        axisX.setHasTiltedLabels(true);
        axisX.setMaxLabelChars(6);//设置标签与坐标轴的距离
        axisX.setAutoGenerated(true);//自动产生标签,以便于查看柱体个数，若程序调试成功，可将其注释掉，便能显示柱体生成时间了
        
        //初始化Y轴
        axisY.setName("能量值");//设置竖轴名称
        axisY.setHasLines(true);
        axisY.setLineColor(Color.BLACK);
        axisY.setHasTiltedLabels(true);
        axisY.setAutoGenerated(true);
      
        mColumnChartData.setFillRatio(0.66f);
        mColumnChartData.setAxisXBottom(axisX); //设置横轴
        mColumnChartData.setAxisYLeft(axisY);   //设置竖轴
        mColumnChartView.setInteractive(true);//设置图表是可以交互的（拖拽，缩放等效果的前提）
        mColumnChartView.setMaxZoom(currentNum/6+66);//按照柱体数量增加缩放次数
        mColumnChartView.setColumnChartData(mColumnChartData);

        //下面的代码放在setColumnChartData之前是无法得到有效执行的
        Viewport v = new Viewport(mColumnChartView.getMaximumViewport());
        v.top = 105;
        v.bottom= 0;
        mColumnChartView.setMaximumViewport(v);
        v.left= currentNum-7;//从最右边最新的数据开始进行显示
        v.right=currentNum;
        mColumnChartView.setCurrentViewport(v);

    }
	
	//增加一个柱子的方法
	private void addcolumn(String strPower) {
		
		//Toast.makeText(getActivity(), "进行数据的更新", Toast.LENGTH_SHORT).show();
		float data = (float) Float.parseFloat(strPower);//转换为浮点型数据
		SimpleDateFormat sDateFormat = new SimpleDateFormat("HH:mm:ss");
		String label = sDateFormat.format(new Date());
		addOneItem(label,data);
	}
	
	//一次添加一个数据的方法
    private void addOneItem(String label,float data){
    	int count = columnList.size(); //得到当前柱体的数量
    	xValues.add(data);
    	xLabel.add(label);
    	//添加数据
    	List<SubcolumnValue> subcolumnValueList = new ArrayList<SubcolumnValue>();//保存子柱子相关值
        subcolumnValueList.add(new SubcolumnValue((float) xValues.get(count),ChartUtils.COLOR_GREEN));
        Column column = new Column(subcolumnValueList);//新建一个柱子
        columnList.add(column);//在柱子列表中另行的添加一个柱子
        mColumnChartData = new ColumnChartData(columnList);//生成柱子的数据实例
        axisValues.add(new AxisValue(count).setLabel(xLabel.get(count)));//给该柱子添加相关的坐标轴数据
        //必要属性设置
        
        column.setHasLabels(true);//设置柱体标签
        column.setHasLabelsOnlyForSelected(false);//固定显示，而非在得到点击后才进行显示
        ColumnChartValueFormatter formatter = new SimpleColumnChartValueFormatter(2);//设置小数位个数
        column.setFormatter(formatter);
        //柱体相关的配置
        setColumnConfig(columnList.size());
    }
}
