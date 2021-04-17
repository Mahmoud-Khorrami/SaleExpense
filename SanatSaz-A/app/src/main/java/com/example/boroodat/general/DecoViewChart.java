package com.example.boroodat.general;

import android.graphics.Color;
import android.graphics.PointF;
import android.view.animation.OvershootInterpolator;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.EdgeDetail;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

public class DecoViewChart
{
    public DecoView decoView;

    public DecoViewChart(DecoView decoView)
    {
        this.decoView = decoView;
    }

    public void setDeco()
    {
        decoView.addSeries(new SeriesItem.Builder(Color.parseColor("#959595"))
                .setRange(0, 100, 100)
                .setInitialVisibility(false)
                .setLineWidth(20f)
                .build());

        decoView.configureAngles(360,0);

        decoView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                .setDelay(100)
                .setDuration(500)
                .build());

        final SeriesItem seriesItem1 = new SeriesItem.Builder(Color.parseColor("#7AC9E8"))
                .setRange(0, 100, 0)
                .setInitialVisibility(false)
                .setLineWidth(50f)
                .addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_OUTER, Color.parseColor("#22000000"), 0.4f))
                //.setSeriesLabel(new SeriesLabel.Builder("باقیمانده تنخواه %.0f%%").build())
                .setInterpolator(new OvershootInterpolator())
                .setShowPointWhenEmpty(false)
                .setCapRounded(false)
                .setInset(new PointF(-30f, -30f))
                .setDrawAsPoint(false)
                .setSpinClockwise(true)
                .setSpinDuration(1000)
                .setChartStyle(SeriesItem.ChartStyle.STYLE_DONUT)
                .build();


        int series1Index = decoView.addSeries(seriesItem1);

        decoView.addEvent(new DecoEvent.Builder(75)
                .setIndex(series1Index)
                .setColor(Color.parseColor("#3DABF6"))
                .setDelay(3000)
                .setListener(new DecoEvent.ExecuteEventListener()
                {
                    @Override
                    public void onEventStart(DecoEvent event)
                    {
                        //Toast.makeText(getContext(),"start",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onEventEnd(DecoEvent event)
                    {

                        //Toast.makeText(getContext(),"end",Toast.LENGTH_LONG).show();
                    }
                })
                .build());

    }
}
