package org.openforis.collect.presenter {
	import flash.events.TimerEvent;
	import flash.utils.Timer;
	
	import mx.rpc.AsyncResponder;
	import mx.rpc.events.ResultEvent;
	
	import org.openforis.collect.Application;
	import org.openforis.collect.client.ClientFactory;
	import org.openforis.collect.concurrency.CollectJobStatusPopUp;
	import org.openforis.collect.event.CollectJobEvent;
	import org.openforis.concurrency.proxy.JobProxy;

	/**
	 * @author S. Ricci
	 */
	public class CollectJobMonitor extends AbstractPresenter {
		
		private static const STATUS_UPDATE_DELAY:int = 2000;
		
		private var _progressTimer:Timer;
		private static var _job:JobProxy;

		public function CollectJobMonitor() {
			super(null);
		}
		
		public function startProgressTimer():void {
			if ( _progressTimer == null ) {
				_progressTimer = new Timer(STATUS_UPDATE_DELAY);
				_progressTimer.addEventListener(TimerEvent.TIMER, progressTimerHandler);
			}
			_progressTimer.start();
		}
		
		public static function get currentJob():JobProxy {
			return _job;
		}
		
		protected function stopProgressTimer():void {
			if ( _progressTimer != null ) {
				_progressTimer.stop();
				_progressTimer = null;
			}
		}
		
		protected function progressTimerHandler(event:TimerEvent):void {
			if (Application.serverOffline) {
				stopProgressTimer();
			} else {
				loadCurrentJobAndUpdateState();
			}
		}
		
		private function loadCurrentJobAndUpdateState():void {
			var oldJob:JobProxy = _job;
			
			if (_job == null) {
				//job complete?
				if (CollectJobStatusPopUp.popUpOpen && CollectJobStatusPopUp.currentInstance.job != null) {
					//use popup current job
					_job = CollectJobStatusPopUp.currentInstance.job;
				}
			}
			if (_job == null) {
				loadApplicationJob(function():void {
					if (_job == null && Application.activeSurvey != null) {
						loadSurveyJob(function():void {
							onJobLoaded();
						});
					} else {
						onJobLoaded();
					}
				});
			} else {
				loadJob(_job.id, function():void {
					onJobLoaded();
				});
			}
			
			function onJobLoaded():void {
				var jobChanged:Boolean = _job != null && (oldJob == null || oldJob.id != _job.id);
				var jobStatusChanged:Boolean = _job != null && oldJob != null && oldJob.status != _job.status;
				/*
				trace("job loaded: " + (_job == null ? null : _job.id) + " - status: " + (_job == null ? null: _job.status) 
					+ " - changed: " + jobChanged + " - status changed: " + jobStatusChanged);
				*/
				if (_job != null && (_job.running || jobChanged || jobStatusChanged)) {
					if (_job.running) {
						if (CollectJobStatusPopUp.popUpOpen) {
							CollectJobStatusPopUp.setActiveJob(_job);
						} else {
							CollectJobStatusPopUp.openPopUp(_job);
						}
					}
					dispatchJobUpdateEvent();
					
					if (! (_job.pending || _job.running)) {
						_job = null;
					}
				}
			}
		}
		
		private function loadApplicationJob(callback:Function):void {
			ClientFactory.collectJobClient.getApplicationJob(new AsyncResponder(
				function(event:ResultEvent, token:Object = null):void {
					_job = event.result as JobProxy;
					callback();
				}, faultHandler
			));
		}
		
		private function loadSurveyJob(callback:Function):void {
			ClientFactory.collectJobClient.getSurveyJob(new AsyncResponder(
				function(event:ResultEvent, token:Object = null):void {
					_job = event.result as JobProxy;
					callback();
				}, faultHandler
			), Application.activeSurvey.id);
		}
		
		private function loadJob(id:String, callback:Function):void {
			ClientFactory.collectJobClient.getJob(new AsyncResponder(
				function(event:ResultEvent, token:Object = null):void {
					_job = event.result as JobProxy;
					callback();
				}, faultHandler
			), id);
		}

		private function dispatchJobUpdateEvent():void {
			if (_job != null) {
				//trace("dispatch job update event - job status: " + _job.status);

				eventDispatcher.dispatchEvent(new CollectJobEvent(CollectJobEvent.COLLECT_JOB_STATUS_UPDATE, _job));
				if (_job.completed || _job.aborted || _job.failed) {
					eventDispatcher.dispatchEvent(new CollectJobEvent(CollectJobEvent.COLLECT_JOB_END, _job));
				}
			}
		}
	}
}